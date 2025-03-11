package com.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class PaymentConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database;

    @Valid
    @NotNull
    @JsonProperty("flyway")
    private FlywayFactory flyway;

    public void setDatabase(DataSourceFactory database) {
        this.database = database;
    }

    public DataSourceFactory getDatabase() {
        return database;
    }

    public void setFlyway(FlywayFactory flyway) {
        this.flyway = flyway;
    }

    public FlywayFactory getFlyway() {
        return flyway;
    }

}
