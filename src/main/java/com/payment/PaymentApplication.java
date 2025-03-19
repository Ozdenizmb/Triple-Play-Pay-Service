package com.payment;

import com.payment.config.LiquibaseFactory;
import com.payment.controller.PaymentController;
import com.payment.repository.ChargeRepository;
import com.payment.repository.RefundRepository;
import com.payment.service.PaymentService;
import com.payment.service.api.TriplePlayPayService;
import com.payment.service.impl.PaymentServiceImpl;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.sql.Connection;
import java.util.EnumSet;
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

        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");


        // JDBI was enabled in the relevant database.
        Jdbi jdbi = Jdbi.create(configuration.getDatabase().getUrl(),
                Objects.requireNonNull(configuration.getDatabase().getUser()),
                Objects.requireNonNull(configuration.getDatabase().getPassword()));
        jdbi.installPlugin(new SqlObjectPlugin());

        // Create DAO
        ChargeRepository chargeRepository = jdbi.onDemand(ChargeRepository.class);
        RefundRepository refundRepository = jdbi.onDemand(RefundRepository.class);

        // Create Service
        TriplePlayPayService triplePlayPayService = new TriplePlayPayService(configuration.getTriplePlayPayApi().getUrl(), configuration.getTriplePlayPayApi().getApiKey());
        PaymentService paymentService = new PaymentServiceImpl(chargeRepository, refundRepository, triplePlayPayService);

        // Saving REST API resource
        environment.jersey().register(new PaymentController(paymentService));
    }
}