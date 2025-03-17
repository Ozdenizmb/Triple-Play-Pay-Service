package com.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.payment.config.LiquibaseFactory;
import com.payment.model.TriplePlayPayFactory;
import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database;

    @Valid
    @NotNull
    @JsonProperty("liquibase")
    private LiquibaseFactory liquibase;

    @Valid
    @NotNull
    @JsonProperty("triplePlayPayApi")
    private TriplePlayPayFactory triplePlayPayApi;

}
