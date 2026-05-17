-- V2__alter_users_table.sql
-- 1. remove constraint de unicidade do cpf
ALTER TABLE users DROP CONSTRAINT UQ__users__D836E71FDBA63E2F;

-- 2. dropa coluna cpf
ALTER TABLE users DROP COLUMN cpf;

-- 3. remove constraint de default do balance
ALTER TABLE users DROP CONSTRAINT DF__users__balance__5EBF139D;

-- 4. dropa coluna balance
ALTER TABLE users DROP COLUMN balance;