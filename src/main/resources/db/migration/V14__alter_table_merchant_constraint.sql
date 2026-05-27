ALTER TABLE merchant
DROP CONSTRAINT UQ__merchant__C0F9E57F90C57F7D;

CREATE UNIQUE INDEX uq_merchant_base_url
    ON merchant(base_url)
    WHERE base_url IS NOT NULL;