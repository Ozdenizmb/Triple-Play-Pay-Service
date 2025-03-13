package com.payment.controller;

import com.payment.api.PaymentApi;
import com.payment.model.ApiResponse;
import com.payment.model.ProcessedTransaction;
import com.payment.service.PaymentService;
import jakarta.ws.rs.core.Response;

public class PaymentController implements PaymentApi {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public Response refundPayment() {
        ApiResponse<ProcessedTransaction> apiResponse = paymentService.refundPayment();
        return Response.ok(apiResponse).build();
    }

}
