-- V2__create_merchants_table.sql
CREATE TABLE merchant (
                          id       BIGINT        IDENTITY(1,1) PRIMARY KEY,
                          base_url VARCHAR(255)  NOT NULL UNIQUE,
                          user_id  BIGINT        NOT NULL UNIQUE, -- UNIQUE pq é OneToOne
                          CONSTRAINT fk_merchant_user FOREIGN KEY (user_id) REFERENCES users(id)
);