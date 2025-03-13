package com.payment.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChargeRequest {
    double amount;
    @JsonProperty("credit_card_number")
    String creditCardNumber;
    @JsonProperty("expiration_month")
    String expirationMonth;
    @JsonProperty("expiration_year")
    String expirationYear;
    String cvv;
}
