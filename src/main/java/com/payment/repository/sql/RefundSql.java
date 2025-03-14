package com.payment.repository.sql;

public class RefundSql {

    private RefundSql(){}

    public static final String INSERT_REFUND = "INSERT INTO " +
            "util_sch.refund_payment(transaction_id, method, original_transaction_id, amount, ip, status) " +
            "VALUES (:transactionId, :method, :originalTransactionId, :amount, :ip, :status)";

    public static final String SELECT_REFUND_WITH_ID = "SELECT * FROM util_sch.refund_payment " +
            "WHERE transaction_id = :transactionId";

    public static final String SELECT_REFUNDS = "SELECT * FROM util_sch.refund_payment";

    public static final String DELETE_REFUND = "DELETE FROM util_sch.refund_payment WHERE transaction_id = :transactionId";

}
