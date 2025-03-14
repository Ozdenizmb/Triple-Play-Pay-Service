package com.payment.mapper;

import com.payment.model.ChargePayment;
import com.payment.model.api.ProcessedTransaction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ChargeMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static ChargePayment toChargePayment(ProcessedTransaction processedTransaction) {
        ChargePayment chargePayment = new ChargePayment();
        Map<String, Object> details = processedTransaction.getDetails();

        chargePayment.setTransactionId(processedTransaction.getTransactionId());
        chargePayment.setMethod(processedTransaction.getMethod());
        chargePayment.setAmount(processedTransaction.getAmount());

        chargePayment.setToken(processedTransaction.getToken());
        chargePayment.setIp(processedTransaction.getIp());

        if(details != null) {
            chargePayment.setCurrency(details.get("currency").toString());

            String transactionDateStr = details.get("transactionApprovalDate").toString();
            String transactionTimeStr = details.get("transactionApprovalTime").toString();
            chargePayment.setTransactionDate(LocalDate.parse(transactionDateStr, DATE_FORMATTER));
            chargePayment.setTransactionTime(LocalTime.parse(transactionTimeStr, TIME_FORMATTER));

            Map<String, Object> creditCard = (Map<String, Object>) details.get("creditCard");
            chargePayment.setCardType(creditCard.get("cardType").toString());
            chargePayment.setCardLastFourDigit(creditCard.get("cardLastFourDigits").toString());
            chargePayment.setExpirationMonth(creditCard.get("expirationMonth").toString());
            chargePayment.setExpirationYear(creditCard.get("expirationYear").toString());
        }

        chargePayment.setStatus(true);

        return chargePayment;
    }
}
