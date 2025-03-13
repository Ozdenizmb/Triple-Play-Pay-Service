package com.payment.service;

import com.payment.model.ApiResponse;
import com.payment.model.ProcessedTransaction;

import java.util.UUID;

public interface PaymentService {

    ApiResponse<ProcessedTransaction> voidPayment(UUID transactionId);

}
