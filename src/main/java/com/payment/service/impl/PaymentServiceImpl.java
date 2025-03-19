package com.payment.service.impl;

import com.payment.exception.ServiceException;
import com.payment.mapper.ChargeMapper;
import com.payment.mapper.RefundMapper;
import com.payment.model.ChargePayment;
import com.payment.model.RefundPayment;
import com.payment.model.api.ApiResponse;
import com.payment.model.api.ProcessedTransaction;
import com.payment.model.request.CardRequest;
import com.payment.model.request.ChargeRequest;
import com.payment.repository.ChargeRepository;
import com.payment.repository.RefundRepository;
import com.payment.service.PaymentService;
import com.payment.service.api.TriplePlayPayService;
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
    private final TriplePlayPayService triplePlayPayService;

    public PaymentServiceImpl(ChargeRepository chargeRepository, RefundRepository refundRepository, TriplePlayPayService triplePlayPayService) {
        this.chargeRepository = chargeRepository;
        this.refundRepository = refundRepository;
        this.triplePlayPayService = triplePlayPayService;
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
    public String cardToken(CardRequest cardRequest) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cc", cardRequest.getCreditCardNumber());
        requestBody.put("mm", cardRequest.getExpirationMonth());
        requestBody.put("yy", cardRequest.getExpirationYear());
        requestBody.put("cvv", cardRequest.getCvv());

        ApiResponse<String> responseApi = triplePlayPayService.postForString("card", requestBody);
        return responseApi.getMessage();
    }

    @Override
    public ApiResponse<ProcessedTransaction> chargePayment(ChargeRequest chargeRequest) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", chargeRequest.getAmount());
        requestBody.put("token", chargeRequest.getToken());

        ApiResponse<ProcessedTransaction> responseApi = triplePlayPayService.postForProcessedTransaction("charge", requestBody);
        changeDatabase("charge_payment", "insert", responseApi.getMessage());
        return responseApi;
    }

    @Override
    public ApiResponse<ProcessedTransaction> voidPayment(UUID transactionId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", transactionId.toString());

        ApiResponse<ProcessedTransaction> responseApi = triplePlayPayService.postForProcessedTransaction("void", requestBody);

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
