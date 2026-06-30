#!/usr/bin/env bash
set -euo pipefail

BASE="http://localhost:8080"
INTERNAL_TOKEN="7tL7QmqYWrkNL6SvKZarWpRMsZzIADaZwZAFn2Hlmlc="

ok() { echo -e "\033[32m[OK]\033[0m $1"; }
fail() { echo -e "\033[31m[FAIL]\033[0m $1"; exit 1; }
step() { echo -e "\n\033[1;34m=== PASSO $1 ===\033[0m"; }

check_status() {
  local step="$1" status="$2" body="$3"
  echo "Status HTTP: $status"
  echo "Body: $body"
  if [[ "$status" -lt 200 || "$status" -ge 300 ]]; then
    fail "Passo $step falhou com status $status"
  fi
  ok "Passo $step concluído"
}

# ─── PASSO 1: Registrar merchant ────────────────────────────────────────────
step "1 — Registrar merchant"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"name":"Loja Teste","email":"loja@teste.com","password":"Senha@123"}')
BODY=$(echo "$RESP" | head -n -1)
STATUS=$(echo "$RESP" | tail -n 1)
echo "Status HTTP: $STATUS"
echo "Body: $BODY"
if [[ "$STATUS" == "201" ]]; then
  ok "Passo 1 — merchant registrado com sucesso"
elif echo "$BODY" | grep -qi "already registered"; then
  ok "Passo 1 — merchant já existe, seguindo com login"
else
  fail "Passo 1 falhou com status $STATUS"
fi

# ─── PASSO 2: Login (captura JWT) ───────────────────────────────────────────
step "2 — Login"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"loja@teste.com","password":"Senha@123"}')
BODY=$(echo "$RESP" | head -n -1)
STATUS=$(echo "$RESP" | tail -n 1)
check_status 2 "$STATUS" "$BODY"
JWT=$(echo "$BODY" | jq -r '.token')
[[ "$JWT" == "null" || -z "$JWT" ]] && fail "token não encontrado na resposta do login"
echo "JWT capturado: ${JWT:0:30}..."

# ─── PASSO 3: Configurar baseUrl do merchant ─────────────────────────────────
step "3 — Configurar baseUrl"
RESP=$(curl -s -w "\n%{http_code}" -X PUT "$BASE/app/merchant/base-url" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT" \
  -d '{"baseUrl":"https://webhook.site/437fb65c-89b8-4f41-87f8-b7773ac19abc"}')
BODY=$(echo "$RESP" | head -n -1)
STATUS=$(echo "$RESP" | tail -n 1)
check_status 3 "$STATUS" "$BODY"

# ─── PASSO 4: Gerar API Key (captura Key) ────────────────────────────────────
step "4 — Gerar API Key"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE/app/api-key/generate" \
  -H "Authorization: Bearer $JWT")
BODY=$(echo "$RESP" | head -n -1)
STATUS=$(echo "$RESP" | tail -n 1)
check_status 4 "$STATUS" "$BODY"
API_KEY=$(echo "$BODY" | jq -r '.Key')
[[ "$API_KEY" == "null" || -z "$API_KEY" ]] && fail "Key não encontrada na resposta de api-key/generate"
echo "API Key capturada: ${API_KEY:0:20}..."

# ─── PASSO 5: Criar webhook subscription ────────────────────────────────────
step "5 — Criar webhook subscription"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE/app/webhooksubscription/create-webhook-subscription" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT" \
  -d '{"path":"charges/paid","event":"CHARGE_PAID"}')
BODY=$(echo "$RESP" | head -n -1)
STATUS=$(echo "$RESP" | tail -n 1)
echo "Status HTTP: $STATUS"
echo "Body: $BODY"
if [[ "$STATUS" == "201" ]]; then
  ok "Passo 5 — webhook subscription criada"
elif [[ "$STATUS" == "409" ]] && echo "$BODY" | grep -qi "already exists"; then
  ok "Passo 5 — webhook subscription já existe, seguindo"
else
  fail "Passo 5 falhou com status $STATUS"
fi

# ─── PASSO 6: Criar charge PIX (usa X-Api-Key, captura id) ──────────────────
step "6 — Criar charge PIX"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE/api/v1/charge/new-charge/" \
  -H "Content-Type: application/json" \
  -H "X-Api-Key: $API_KEY" \
  -d "{\"amount\":49.90,\"paymentType\":\"PIX\",\"externalId\":\"pedido-$(date +%s)\"}")
BODY=$(echo "$RESP" | head -n -1)
STATUS=$(echo "$RESP" | tail -n 1)
check_status 6 "$STATUS" "$BODY"
CHARGE_ID=$(echo "$BODY" | jq -r '.id')
[[ "$CHARGE_ID" == "null" || -z "$CHARGE_ID" ]] && fail "id não encontrado na resposta da charge"
echo "Charge ID capturado: $CHARGE_ID"

# ─── PASSO 7: Simular pagamento aprovado (dispara webhook) ───────────────────
step "7 — Simular pagamento aprovado"
RESP=$(curl -s -w "\n%{http_code}" -X POST "$BASE/internal/payment/simulate" \
  -H "Content-Type: application/json" \
  -H "X-Internal-Token: $INTERNAL_TOKEN" \
  -d "{\"chargeId\":$CHARGE_ID,\"result\":\"APPROVED\"}")
BODY=$(echo "$RESP" | head -n -1)
STATUS=$(echo "$RESP" | tail -n 1)
check_status 7 "$STATUS" "$BODY"

echo -e "\n\033[1;32m=============================="
echo " FLUXO E2E COMPLETO COM SUCESSO"
echo "==============================\033[0m"
echo "O webhook deve ter sido disparado para:"
echo "  https://webhook.site/437fb65c-89b8-4f41-87f8-b7773ac19abc"
