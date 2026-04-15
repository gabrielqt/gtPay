-- V1__create_users_table.sql

CREATE TABLE users (
                       id       BIGINT        IDENTITY(1,1) PRIMARY KEY, -- IDENTITY = auto increment no SQL Server, equivalente ao @GeneratedValue(IDENTITY)
                       name     VARCHAR(150)  NOT NULL,                  -- @NotNull na entidade
                       email    VARCHAR(150)  NOT NULL UNIQUE,            -- @Email + usado como username no login
                       password VARCHAR(255)  NOT NULL,                  -- BCrypt gera hash de ~60 chars, 255 é margem segura
                       cpf      VARCHAR(14)   NOT NULL UNIQUE,            -- @CPF — formato "000.000.000-00" = 14 chars
                       role     VARCHAR(50)   NOT NULL,                  -- @Enumerated(EnumType.STRING) salva o nome do enum ex: "ADMIN"
                       balance  DECIMAL(19,2) NOT NULL DEFAULT 0.00      -- BigDecimal no Java → DECIMAL no SQL, 19,2 é padrão pra valores monetários
);