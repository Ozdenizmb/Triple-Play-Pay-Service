package com.payment;

import com.payment.controller.PaymentController;
import com.payment.service.impl.PaymentServiceImpl;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

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
        final PaymentServiceImpl paymentService = new PaymentServiceImpl(configuration.getTriplePlayPayApi().getUrl(), configuration.getTriplePlayPayApi().getApiKey());

        final PaymentController paymentController = new PaymentController(paymentService);
        environment.jersey().register(paymentController);
    }
}