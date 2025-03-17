package com.payment;

import com.payment.config.LiquibaseFactory;
import com.payment.controller.PaymentController;
import com.payment.repository.ChargeRepository;
import com.payment.repository.RefundRepository;
import com.payment.service.PaymentService;
import com.payment.service.impl.PaymentServiceImpl;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.sql.Connection;
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

        // Tables were created on the database with Liquibase.
        LiquibaseFactory liquibaseFactory = configuration.getLiquibase();
        Connection connection = configuration.getDatabase().build(environment.metrics(), "liquibase").getConnection();
        Liquibase liquibase = new Liquibase(liquibaseFactory.getChangelog(), new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
        liquibase.update(configuration.getLiquibase().getContexts());


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