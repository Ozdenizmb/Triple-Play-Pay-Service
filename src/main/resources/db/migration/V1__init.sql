CREATE SCHEMA IF NOT EXISTS util_sch;

CREATE TABLE IF NOT EXISTS util_sch.charge_payment
(
    transaction_id            uuid NOT NULL,
    method                    VARCHAR NOT NULL,
    amount                    DOUBLE PRECISION NOT NULL,
    currency                  VARCHAR NOT NULL,
    transaction_date          DATE NOT NULL,
    transaction_time          TIME NOT NULL,
    token                     VARCHAR NOT NULL,
    ip                        VARCHAR NOT NULL,
    card_type                 VARCHAR,
    card_last_four_digit      VARCHAR,
    expiration_month          VARCHAR,
    expiration_year           VARCHAR,
    status                    BOOLEAN NOT NULL,
    PRIMARY KEY (transaction_id)
);