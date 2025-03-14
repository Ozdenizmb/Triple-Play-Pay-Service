package com.payment.model;

import lombok.Data;

import java.util.UUID;

@Data
public class RefundPayment {

    private UUID transactionId;
    private String method;
    private UUID originalTransactionId;
    private Double amount;
    private String ip;
    private Boolean status;

}
