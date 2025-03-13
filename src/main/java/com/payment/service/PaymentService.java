package com.payment.service;

import com.payment.model.ApiResponse;
import com.payment.model.ProcessedTransaction;

public interface PaymentService {

    ApiResponse<ProcessedTransaction> refundPayment();

}
