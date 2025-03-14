package com.payment;

import com.payment.controller.PaymentController;
import com.payment.service.PaymentService;
import com.payment.service.impl.PaymentServiceImpl;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.flywaydb.core.Flyway;

public class PaymentApplication extends Application<PaymentConfiguration> {

    public static void main(String[] args) throws Exception {
        new PaymentApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<PaymentConfiguration> bootstrap) {
        super.initialize(bootstrap);
    }

    @Override
    public void run(PaymentConfiguration configuration, Environment environment) throws Exception {

        // Tables were created on the database with Flyway.
        Flyway flyway = Flyway.configure()
                .dataSource(configuration.getDatabase().getUrl(),
                        configuration.getDatabase().getUser(),
                        configuration.getDatabase().getPassword())
                .locations(configuration.getFlyway().getLocations().toArray(new String[0]))
                .load();
        flyway.migrate();

        // Create Service
        PaymentService paymentService = new PaymentServiceImpl(configuration.getTriplePlayPayApi().getUrl(), configuration.getTriplePlayPayApi().getApiKey());

        // Saving REST API resource
        environment.jersey().register(new PaymentController(paymentService));
    }
}