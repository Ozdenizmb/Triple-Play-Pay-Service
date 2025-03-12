package com.payment.service.impl;

import com.payment.model.ApiResponse;
import com.payment.model.ProcessedTransaction;
import com.payment.service.PaymentService;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;

public class PaymentServiceImpl implements PaymentService {

    private final Client client;
    private final String apiUrl;
    private final String apiKey;

    public PaymentServiceImpl(String apiUrl, String apiKey) {
        this.client = ClientBuilder.newClient();
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    @Override
    public ApiResponse<ProcessedTransaction> getEventsForPayment() {
        WebTarget target = client.target(apiUrl)
                .path("events")
                .queryParam("api_key", apiKey);
        System.out.println(apiKey);
        return target.request(MediaType.APPLICATION_JSON)
                .get(new GenericType<>() {});
    }
}
