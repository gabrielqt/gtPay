-- V10__alter_merchant_base_url_nullable.sql
ALTER TABLE webhook_subscription
ALTER COLUMN path VARCHAR(255);

ALTER TABLE webhook_subscription
    ADD CONSTRAINT uq_webhook_subscription_path UNIQUE (path);