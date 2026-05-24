ALTER TABLE webhook_subscription
    ADD CONSTRAINT uq_webhook_subscription_merchant_path
        UNIQUE (merchant_id, path);