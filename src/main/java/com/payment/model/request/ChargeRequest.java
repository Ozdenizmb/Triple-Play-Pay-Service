package com.payment.model.request;

import lombok.Data;

@Data
public class ChargeRequest {
    double amount;
    String token;
}
