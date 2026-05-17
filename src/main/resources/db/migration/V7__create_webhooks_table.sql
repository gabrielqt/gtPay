-- V6__create_webhook_tables.sql
CREATE TABLE webhook_subscription (
                                      id          BIGINT       IDENTITY(1,1) PRIMARY KEY,
                                      merchant_id BIGINT       NOT NULL,
                                      path        VARCHAR(255) NOT NULL,
                                      is_active   BIT          NOT NULL DEFAULT 1,
                                      secret      VARCHAR(255) NOT NULL,
                                      event       VARCHAR(50)  NOT NULL,
                                      CONSTRAINT fk_subscription_merchant FOREIGN KEY (merchant_id) REFERENCES merchant(id)
);

CREATE TABLE webhook_event (
                               id              BIGINT       IDENTITY(1,1) PRIMARY KEY,
                               type            VARCHAR(50)  NOT NULL,
                               payload         TEXT         NOT NULL,
                               delivered       BIT          NOT NULL DEFAULT 0,
                               attempts        INT          NOT NULL DEFAULT 0,
                               last_attempt_at DATETIME,
                               charge_id       BIGINT       NOT NULL,
                               subscription_id BIGINT       NOT NULL,
                               CONSTRAINT fk_event_charge       FOREIGN KEY (charge_id)       REFERENCES charge(id),
                               CONSTRAINT fk_event_subscription FOREIGN KEY (subscription_id) REFERENCES webhook_subscription(id)
);