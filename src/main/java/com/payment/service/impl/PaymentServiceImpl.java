package com.payment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import com.payment.exception.ApiException;
import com.payment.model.ApiResponse;
import com.payment.model.ProcessedTransaction;
import com.payment.service.PaymentService;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaymentServiceImpl implements PaymentService {

    private final Client client;
    private final String apiUrl;
    private final String apiKey;

    public PaymentServiceImpl(String apiUrl, String apiKey) {
        ObjectMapper mapper = new ObjectMapper();
        this.client = ClientBuilder.newClient().register(new JacksonJsonProvider(mapper));
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    private WebTarget target(String path) {
        return client.target(apiUrl).path(path);
    }

    private <T> T performPost(String path, Object requestBody, GenericType<T> responseType) {
        WebTarget target = target(path);
        Invocation.Builder builder = target
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", apiKey);
        System.out.println("ApiKey: " + apiKey);
        System.out.println("Path:" + target.getUri());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody;

        try {
            jsonBody = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing request body to JSON", e);
        }

        Response response = builder.post(Entity.json(jsonBody));
        //Response response = builder.get();

        if(response.getStatus() >= 400) {
            String errorMessage = response.readEntity(String.class);
            System.out.println("Error calling {}: {} " + path + " " + errorMessage);
            throw new ApiException("Error calling " + path + ": " + errorMessage, response);
        }

        return response.readEntity(responseType);
    }

    @Override
    public ApiResponse<ProcessedTransaction> refundPayment() {
        UUID transactionId = UUID.fromString("844fc958-783b-4b31-9282-771ff1a4d31e");
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", transactionId.toString());
        return performPost("void", requestBody, new GenericType<ApiResponse<ProcessedTransaction>>() {});
    }
}
