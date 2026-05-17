-- V5__create_payments_table.sql
CREATE TABLE payments (
                          id         BIGINT        IDENTITY(1,1) PRIMARY KEY,
                          type       VARCHAR(20)   NOT NULL, -- discriminator: PIX ou CARD
                          amount     DECIMAL(19,2) NOT NULL,
                          created_at DATETIME      NOT NULL
);

CREATE TABLE payments_pix (
                              id      BIGINT       NOT NULL PRIMARY KEY,
                              pix_key VARCHAR(150) NOT NULL,
                              CONSTRAINT fk_pix_payment FOREIGN KEY (id) REFERENCES payments(id)
);

CREATE TABLE payments_card (
                               id          BIGINT       NOT NULL PRIMARY KEY,
                               last_digits INT          NOT NULL,
                               card_brand  VARCHAR(30)  NOT NULL,
                               holder_name VARCHAR(150) NOT NULL,
                               CONSTRAINT fk_card_payment FOREIGN KEY (id) REFERENCES payments(id)
)