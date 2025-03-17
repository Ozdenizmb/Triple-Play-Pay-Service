package com.payment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import com.payment.exception.ApiException;
import com.payment.exception.ServiceException;
import com.payment.mapper.ChargeMapper;
import com.payment.mapper.RefundMapper;
import com.payment.model.ChargePayment;
import com.payment.model.RefundPayment;
import com.payment.model.api.ApiResponse;
import com.payment.model.api.ProcessedTransaction;
import com.payment.model.request.ChargeRequest;
import com.payment.repository.ChargeRepository;
import com.payment.repository.RefundRepository;
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
import java.util.Objects;
import java.util.UUID;

import static com.payment.exception.ErrorMessages.WRONG_TABLE_NAME;
import static com.payment.exception.ErrorMessages.WRONG_TRANSACTION_NAME;

public class PaymentServiceImpl implements PaymentService {

    private final ChargeRepository chargeRepository;
    private final RefundRepository refundRepository;

    private final Client client;
    private final String apiUrl;
    private final String apiKey;

    public PaymentServiceImpl(ChargeRepository chargeRepository, RefundRepository refundRepository, String apiUrl, String apiKey) {
        this.chargeRepository = chargeRepository;
        this.refundRepository = refundRepository;
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

        System.out.println("You sent a request to this path:" + target.getUri());

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

    private void changeDatabase(String tableName, String transactionName, ProcessedTransaction data) {
        if(Objects.equals(tableName, "charge_payment")) {
            if(Objects.equals(transactionName, "insert")) {
                ChargePayment chargePayment = ChargeMapper.toChargePayment(data);
                chargeRepository.saveCharge(chargePayment);
            }
            else if(Objects.equals(transactionName, "update_status")) {
                chargeRepository.updateChargeStatus(data.getOriginalTransactionId());
            }
            else {
                throw ServiceException.withStatusAndMessage(Response.Status.INTERNAL_SERVER_ERROR, WRONG_TRANSACTION_NAME);
            }
        }
        else if(Objects.equals(tableName, "refund_payment")) {
            if(Objects.equals(transactionName, "insert")) {
                RefundPayment refundPayment = RefundMapper.toRefundPayment(data);
                refundRepository.saveRefund(refundPayment);
            }
            else {
                throw ServiceException.withStatusAndMessage(Response.Status.INTERNAL_SERVER_ERROR, WRONG_TRANSACTION_NAME);
            }
        }
        else {
            throw ServiceException.withStatusAndMessage(Response.Status.INTERNAL_SERVER_ERROR, WRONG_TABLE_NAME);
        }
    }

    @Override
    public ApiResponse<ProcessedTransaction> chargePayment(ChargeRequest chargeRequest) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", chargeRequest.getAmount());
        requestBody.put("cc", chargeRequest.getCreditCardNumber());
        requestBody.put("mm", chargeRequest.getExpirationMonth());
        requestBody.put("yy", chargeRequest.getExpirationYear());
        requestBody.put("cvv", chargeRequest.getCvv());

        ApiResponse<ProcessedTransaction> responseApi = performPost("charge", requestBody, new GenericType<>() {});
        changeDatabase("charge_payment", "insert", responseApi.getMessage());
        return responseApi;
    }

    @Override
    public ApiResponse<ProcessedTransaction> voidPayment(UUID transactionId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", transactionId.toString());

        ApiResponse<ProcessedTransaction> responseApi = performPost("void", requestBody, new GenericType<>() {});

        /*
         * If the value of responseApi.getStatus() is false, it means that a repeated operation is being performed,
         * meaning an action that has already been done is being executed once again.
         */
        if(responseApi.getStatus()) {
            changeDatabase("refund_payment", "insert", responseApi.getMessage());
            changeDatabase("charge_payment", "update_status", responseApi.getMessage());
        }

        return responseApi;
    }
}
