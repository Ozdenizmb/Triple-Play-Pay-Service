CREATE SCHEMA IF NOT EXISTS util_sch;

CREATE TABLE IF NOT EXISTS util_sch.refund_payment
(
    transaction_id            uuid NOT NULL,
    method                    VARCHAR NOT NULL,
    original_transaction_id   uuid NOT NULL,
    amount                    DOUBLE PRECISION NOT NULL,
    ip                        VARCHAR NOT NULL,
    status                    BOOLEAN NOT NULL,
    PRIMARY KEY (transaction_id)
);