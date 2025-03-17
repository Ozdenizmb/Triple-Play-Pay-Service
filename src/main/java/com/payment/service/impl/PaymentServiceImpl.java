package com.payment.service.impl;

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
import com.payment.service.api.TriplePlayPayApi;
import jakarta.ws.rs.core.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.payment.exception.ErrorMessages.BAD_REQUEST_TPP;
import static com.payment.exception.ErrorMessages.WRONG_TABLE_NAME;
import static com.payment.exception.ErrorMessages.WRONG_TRANSACTION_NAME;

public class PaymentServiceImpl implements PaymentService {

    private final ChargeRepository chargeRepository;
    private final RefundRepository refundRepository;

    private final TriplePlayPayApi triplePlayPayApi;
    private final String apiKey;

    public PaymentServiceImpl(ChargeRepository chargeRepository, RefundRepository refundRepository, String apiUrl, String apiKey) {
        this.chargeRepository = chargeRepository;
        this.refundRepository = refundRepository;
        this.apiKey = apiKey;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        this.triplePlayPayApi = retrofit.create(TriplePlayPayApi.class);
    }

    private <T> T performPost(String path, Map<String, Object> requestBody) {
        try{
            Call<ApiResponse<ProcessedTransaction>> call = triplePlayPayApi.performPost(path, apiKey, requestBody);
            retrofit2.Response<ApiResponse<ProcessedTransaction>> response = call.execute();

            if(!response.isSuccessful()){
                String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                System.out.println("Error calling {}: {} " + path + " " + errorMessage);
                throw ServiceException.withStatusAndMessage(Response.Status.INTERNAL_SERVER_ERROR, errorMessage);
            }

            return (T) response.body();
        } catch (Exception e) {
            throw ServiceException.withStatusAndMessage(Response.Status.INTERNAL_SERVER_ERROR, BAD_REQUEST_TPP);
        }
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

        ApiResponse<ProcessedTransaction> responseApi = performPost("charge", requestBody);
        changeDatabase("charge_payment", "insert", responseApi.getMessage());
        return responseApi;
    }

    @Override
    public ApiResponse<ProcessedTransaction> voidPayment(UUID transactionId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", transactionId.toString());

        ApiResponse<ProcessedTransaction> responseApi = performPost("void", requestBody);

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
