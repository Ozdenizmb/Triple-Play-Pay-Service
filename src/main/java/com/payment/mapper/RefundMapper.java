package com.payment.mapper;

import com.payment.model.RefundPayment;
import com.payment.model.api.ProcessedTransaction;

public class RefundMapper {

    public static RefundPayment toRefundPayment(ProcessedTransaction processedTransaction) {
        RefundPayment refundPayment = new RefundPayment();

        refundPayment.setTransactionId(processedTransaction.getTransactionId());
        refundPayment.setMethod(processedTransaction.getMethod());
        refundPayment.setOriginalTransactionId(processedTransaction.getOriginalTransactionId());
        refundPayment.setAmount(processedTransaction.getAmount());
        refundPayment.setIp(processedTransaction.getIp());
        refundPayment.setStatus(true);

        return refundPayment;
    }

}
