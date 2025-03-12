package com.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.payment.model.TriplePlayPayFactory;
import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;
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
    @JsonProperty("flyway")
    private FlywayFactory flyway;

    @Valid
    @NotNull
    @JsonProperty("triplePlayPayApi")
    private TriplePlayPayFactory triplePlayPayApi;

}
