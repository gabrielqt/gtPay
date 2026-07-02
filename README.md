# gtpay

gateway de pagamentos pix construído em spring boot, com emissão de cobranças, confirmação de pagamento simulada, notificação por webhook (com assinatura hmac e retry automático) e notificação por e-mail — tudo orquestrado via rabbitmq.

> projeto de estudo/portfólio: simula o fluxo de um gateway de pagamentos real (tipo stripe/mercado pago), incluindo autenticação multi-camada, geração de payload pix (brcode) e entrega assíncrona de eventos para os lojistas (merchants).

---

## stack

- **java 17** + **spring boot 4.0.5** (spring framework 7)
- **spring security** -> jwt + api key + internal token (três camadas de auth diferentes)
- **spring data jpa** + **sql server** -> persistência
- **flyway** -> versionamento de schema (20 migrations)
- **rabbitmq** (spring amqp) -> mensageria assíncrona (fanout exchange)
- **spring mail** -> envio de e-mail transacional (smtp gmail)
- **lombok** + **jakarta validation**
- **docker compose** -> app + sql server + rabbitmq, tudo em containers

---

## arquitetura

```
cliente/merchant
      |
      v
[ spring security filters ]  -> jwtfilter | apikeyfilter | internalfilter
      |
      v
[ controllers ]  -> auth | app (merchant) | api/v1 (cobranças) | internal (psp)
      |
      v
[ services ]  -> chargeservice, paymentservice, webhookservice, emailservice...
      |
      v
[ jpa repositories ] -> sql server
      |
      v
[ rabbitmq fanout exchange ] -> webhook-queue + email-queue (consumo assíncrono)
```

### autenticação por prefixo de rota

o projeto usa **três filtros de segurança independentes**, cada um responsável por autenticar um tipo de "principal" diferente, roteados por prefixo de url:

| prefixo | quem autentica | header | principal |
|---|---|---|---|
| `/auth/**` | público | — | — |
| `/app/**` | `jwtfilter` | `authorization: bearer {token}` | `user` (dono da conta/merchant) |
| `/api/v1/**` | `apikeyfilter` | `x-api-key: {key}` | `merchant` (integração server-to-server) |
| `/internal/**` | `internalfilter` | `x-internal-token: {token}` | psp/simulador interno |

essa separação existe porque são consumidores diferentes: o **dono da loja** loga com email/senha e usa jwt pra configurar a conta (`/app/**`); o **sistema do lojista** (backend dele) usa uma api key fixa pra criar cobranças (`/api/v1/**`); e o **simulador de pagamento** (representando o psp/banco) usa um token interno fixo pra confirmar pagamentos (`/internal/**`), sem precisar de login de usuário nenhum.

---

## fluxo de uma cobrança pix, ponta a ponta

1. **merchant se registra** -> `post /auth/register` (senha vai com bcrypt)
2. **login** -> `post /auth/login` retorna jwt
3. **configura loja** -> `put /app/merchant/info` (cep, nome da loja, chave pix) e `put /app/merchant/base-url` (endpoint https que vai receber os webhooks)
4. **gera api key** -> `post /app/api-key/generate`
5. **cria webhook subscription** -> `post /app/webhooksubscription/create-webhook-subscription` (escolhe path + evento: `charge_paid`, `charge_failed`, `charge_expired` ou `all`)
6. **sistema do merchant cria a cobrança** -> `post /api/v1/charge/new-charge/` usando a api key
   - o `brcodeservice` monta o payload pix (emv) na mão -> campos `00`-`62` + crc16, seguindo o padrão do bacen (`br.gov.bcb.pix`)
7. **psp confirma o pagamento** -> `post /internal/payment/simulate` (aprovado ou falho)
   - `paymentservice` valida se a cobrança está `pending` e não expirou (expira em 5 min)
   - atualiza status (`paid`/`failed`/`expired`) e publica um evento no rabbitmq
8. **rabbitmq faz fanout** do evento pra duas filas: `webhook-queue` e `email-queue`
9. **`webhookservice`** consome `webhook-queue` -> busca as subscriptions compatíveis com o status, monta o payload, assina com **hmac** (`x-signature`) e faz `post` pro `base-url` do merchant
   - se a entrega falhar, fica marcada como não entregue e um `@scheduled` job **retenta a cada 60s, até 10 tentativas**
10. **`emailservice`** consome `email-queue` -> busca o e-mail do dono da loja e dispara uma notificação transacional via smtp

```
POST /internal/payment/simulate
        |
        v
  paymentservice
   (valida + atualiza status)
        |
        v
  rabbitTemplate.convertAndSend(charge-exchange, "", MessageCharge)
        |
        +--> webhook-queue --> webhookservice --> POST no base-url do merchant (assinado hmac)
        |                                              |
        |                                        falhou? retry a cada 60s (max 10x)
        |
        +--> email-queue --> emailservice --> smtp (notifica o dono da loja)
```

---

## modelo de dados (principais entidades)

- **user** -> dono da conta, login/senha, papel (`role`)
- **merchant** -> vinculado a um `user`, guarda `base-url`, chave pix, nome da loja, cidade (resolvida a partir do cep via `cepfinder`)
- **apikey** -> chave gerada por merchant, usada nas rotas `/api/v1/**`
- **charge** -> cobrança (pix ou cartão), status (`pending`, `paid`, `failed`, `expired`), `external-id` único por merchant, `brcode` (quando pix)
- **payment** / **paymentcard** -> registro do pagamento efetivado (herança: pix vs cartão)
- **webhooksubscription** -> path + evento que o merchant quer escutar, com secret criptografado pra assinar os payloads
- **webhookevent** -> cada disparo de webhook (payload, tentativas, status de entrega)

---

## segurança

- senhas com **bcrypt**
- jwt assinado com secret via `jwtservice`
- assinatura **hmac** nos payloads de webhook, pra o merchant validar a origem
- secret de cada subscription fica **criptografado** no banco (`secretservice`)
- sessão **stateless** (sem guardar sessão em memória/banco, tudo via token)

---

## rodando localmente

```bash
# variáveis de ambiente (.env na raiz) — db, rabbitmq, jwt secret, internal token, credenciais smtp
docker compose up -d --build
```

sobem 3 containers:
- `gtpay-application` -> api spring boot (porta `8080`, debug remoto na `5005`)
- `sqlserver` -> sql server 2022 (porta `1433`)
- `rabbitmq` -> broker + painel de management (`5672` / `15672`)

o schema é criado automaticamente pelo **flyway** no boot da aplicação.

há um script `test-e2e.sh` que roda o fluxo completo (registro -> login -> configurar loja -> gerar api key -> criar subscription -> criar cobrança -> simular pagamento) e serve como smoke test manual da api inteira.

---
