package com.payment.repository;

import com.payment.model.ChargePayment;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.UUID;

import static com.payment.repository.sql.ChargeSql.DELETE_CHARGE;
import static com.payment.repository.sql.ChargeSql.INSERT_CHARGE;
import static com.payment.repository.sql.ChargeSql.SELECT_CHARGES;
import static com.payment.repository.sql.ChargeSql.SELECT_CHARGE_WITH_ID;

@RegisterBeanMapper(ChargePayment.class)
public interface ChargeRepository {

    @SqlQuery(INSERT_CHARGE)
    UUID saveCharge(@BindBean ChargePayment chargePayment);

    @SqlQuery(SELECT_CHARGE_WITH_ID)
    ChargePayment getChargeById(@Bind("transactionId") UUID transactionId);

    @SqlQuery(SELECT_CHARGES)
    ChargePayment getCharges();

    @SqlUpdate(DELETE_CHARGE)
    Boolean deleteChargeById(@Bind("transactionId") UUID transactionId);

}
