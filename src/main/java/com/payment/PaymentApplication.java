package com.payment;

import com.payment.controller.PaymentController;
import com.payment.repository.ChargeRepository;
import com.payment.repository.RefundRepository;
import com.payment.service.PaymentService;
import com.payment.service.impl.PaymentServiceImpl;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.util.Objects;

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

        // JDBI was enabled in the relevant database.
        Jdbi jdbi = Jdbi.create(configuration.getDatabase().getUrl(),
                Objects.requireNonNull(configuration.getDatabase().getUser()),
                Objects.requireNonNull(configuration.getDatabase().getPassword()));
        jdbi.installPlugin(new SqlObjectPlugin());

        // Create DAO
        ChargeRepository chargeRepository = jdbi.onDemand(ChargeRepository.class);
        RefundRepository refundRepository = jdbi.onDemand(RefundRepository.class);

        // Create Service
        PaymentService paymentService = new PaymentServiceImpl(chargeRepository, refundRepository, configuration.getTriplePlayPayApi().getUrl(), configuration.getTriplePlayPayApi().getApiKey());

        // Saving REST API resource
        environment.jersey().register(new PaymentController(paymentService));
    }
}