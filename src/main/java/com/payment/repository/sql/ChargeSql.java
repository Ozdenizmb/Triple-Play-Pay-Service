package com.payment.repository.sql;

public class ChargeSql {

    private ChargeSql(){}

    public static final String INSERT_CHARGE = "INSERT INTO " +
            "util_sch.charge_payment(transaction_id, method, amount, currency, transaction_date, transaction_time, token, ip, card_type, card_last_four_digit, expiration_month, expiration_year, status) " +
            "VALUES (:transactionId, :method, :amount, :currency, :transactionDate, :transactionTime, :token, :ip, :cardType, :cardLastFourDigit, :expirationMonth, :expirationYear, :status) RETURNING transaction_id";

    public static final String SELECT_CHARGE_WITH_ID = "SELECT * FROM util_sch.charge_payment " +
            "WHERE transaction_id = :transactionId";

    public static final String SELECT_CHARGES = "SELECT * FROM util_sch.charge_payment";

    public static final String DELETE_CHARGE = "DELETE FROM util_sch.charge_payment WHERE transaction_id = :transactionId";

}
