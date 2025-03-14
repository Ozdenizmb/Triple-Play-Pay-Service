package com.payment.repository;

import com.payment.model.RefundPayment;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.UUID;

import static com.payment.repository.sql.RefundSql.DELETE_REFUND;
import static com.payment.repository.sql.RefundSql.INSERT_REFUND;
import static com.payment.repository.sql.RefundSql.SELECT_REFUNDS;
import static com.payment.repository.sql.RefundSql.SELECT_REFUND_WITH_ID;

@RegisterBeanMapper(RefundPayment.class)
public interface RefundRepository {

    @SqlQuery(INSERT_REFUND)
    UUID saveRefund(@BindBean RefundPayment refundPayment);

    @SqlQuery(SELECT_REFUND_WITH_ID)
    RefundPayment getRefundById(@Bind("transactionId") UUID transactionId);

    @SqlQuery(SELECT_REFUNDS)
    RefundPayment getRefunds();

    @SqlUpdate(DELETE_REFUND)
    Boolean deleteRefundById(@Bind("transactionId") UUID transactionId);

}
