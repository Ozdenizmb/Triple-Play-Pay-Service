package com.payment.controller;

import com.payment.api.PaymentApi;
import com.payment.model.api.ApiResponse;
import com.payment.model.api.ProcessedTransaction;
import com.payment.model.request.CardRequest;
import com.payment.model.request.ChargeRequest;
import com.payment.service.PaymentService;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

public class PaymentController implements PaymentApi {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public String cardToken(CardRequest cardRequest) {
        return paymentService.cardToken(cardRequest);
    }

    @Override
    public Response chargePayment(ChargeRequest chargeRequest) {
        ApiResponse<ProcessedTransaction> apiResponse = paymentService.chargePayment(chargeRequest);
        return Response.ok(apiResponse).build();
    }

    @Override
    public Response voidPayment(UUID transactionId) {
        ApiResponse<ProcessedTransaction> apiResponse = paymentService.voidPayment(transactionId);
        return Response.ok(apiResponse).build();
    }

}
