ALTER TABLE charge
DROP CONSTRAINT UQ__charge__ED1FC9EB5634E1A5;

CREATE UNIQUE INDEX uq_charge_payment
    ON charge(payment_id)
    WHERE payment_id IS NOT NULL;