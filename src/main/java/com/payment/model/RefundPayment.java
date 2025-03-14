package com.payment.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class RefundPayment {

    private UUID transactionId;
    private String method;
    private UUID originalTransactionId;
    private BigDecimal amount;
    private String ip;
    private Boolean status;

}
