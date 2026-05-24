ALTER TABLE webhook_subscription
DROP COLUMN secret;

ALTER TABLE webhook_subscription
    ADD secret_encrypted VARCHAR(255) NOT NULL;