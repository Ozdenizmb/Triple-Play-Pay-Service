package com.payment.api;

import com.payment.model.request.CardRequest;
import com.payment.model.request.ChargeRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/api/v1/payments")
@Produces(MediaType.APPLICATION_JSON)
public interface PaymentApi {

    @POST
    @Path("/card")
    String cardToken(CardRequest cardRequest);

    @POST
    @Path("/charge")
    Response chargePayment(ChargeRequest chargeRequest);

    @POST
    @Path("/void/{transaction-id}")
    Response voidPayment(@PathParam("transaction-id") UUID transactionId);

}
