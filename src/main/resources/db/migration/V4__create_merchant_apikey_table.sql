-- V4__create_merchant_apikey_table.sql
CREATE TABLE merchant_apikey (
    id          BIGINT       IDENTITY(1,1) PRIMARY KEY,
    merchant_id BIGINT       NOT NULL,
    key_hash    VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT fk_apikey_merchant FOREIGN KEY (merchant_id) REFERENCES merchant(id)
);