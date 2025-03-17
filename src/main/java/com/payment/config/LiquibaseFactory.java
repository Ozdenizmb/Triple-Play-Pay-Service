package com.payment.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiquibaseFactory {

    @Valid
    @NotNull
    @JsonProperty("changelog")
    private String changelog;

    @Valid
    @JsonProperty("contexts")
    private String contexts;

    @Valid
    @JsonProperty("defaultSchemaName")
    private String defaultSchemaName;

}
