package com.payment.service;

import com.payment.model.ApiResponse;
import com.payment.model.ProcessedTransaction;
import com.payment.model.request.ChargeRequest;

import java.util.UUID;

public interface PaymentService {

    ApiResponse<ProcessedTransaction> chargePayment(ChargeRequest chargeRequest);
    ApiResponse<ProcessedTransaction> voidPayment(UUID transactionId);

}
