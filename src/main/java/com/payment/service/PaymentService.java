package com.payment.service;

import com.payment.model.api.ApiResponse;
import com.payment.model.api.ProcessedTransaction;
import com.payment.model.request.ChargeRequest;

import java.util.UUID;

public interface PaymentService {

    ApiResponse<ProcessedTransaction> chargePayment(ChargeRequest chargeRequest);
    ApiResponse<ProcessedTransaction> voidPayment(UUID transactionId);

}
