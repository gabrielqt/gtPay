-- V6__create_charge_table.sql
CREATE TABLE charge (
                        id          BIGINT        IDENTITY(1,1) PRIMARY KEY,
                        amount      DECIMAL(19,2) NOT NULL,
                        status      VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
                        external_id BIGINT,
                        payment_id  BIGINT        UNIQUE, -- UNIQUE pq é OneToOne
                        merchant_id BIGINT        NOT NULL,
                        created_at  DATETIME      NOT NULL,
                        expires_at  DATETIME,
                        CONSTRAINT fk_charge_payment  FOREIGN KEY (payment_id)  REFERENCES payments(id),
                        CONSTRAINT fk_charge_merchant FOREIGN KEY (merchant_id) REFERENCES merchant(id)
);