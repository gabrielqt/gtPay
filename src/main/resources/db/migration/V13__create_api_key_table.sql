-- V13__create_api_key_table


CREATE TABLE api_key (
                         id BIGINT IDENTITY(1,1) PRIMARY KEY,
                         merchant_id BIGINT,
                         key_hash VARCHAR(255) NOT NULL,

                         CONSTRAINT fk_api_key_merchant
                             FOREIGN KEY (merchant_id)
                                 REFERENCES merchant(id)
);