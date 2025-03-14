package com.payment.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class ChargePayment {

    private UUID transactionId;
    private String method;
    private BigDecimal amount;
    private String currency;
    private LocalDate transactionDate;
    private LocalTime transactionTime;
    private String token;
    private String ip;
    private String cardType;
    private String cardLastFourDigit;
    private String expirationMonth;
    private String expirationYear;
    private Boolean status;

}
