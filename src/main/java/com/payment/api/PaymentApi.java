package com.payment.api;

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
    @Path("/void/{transaction-id}")
    Response voidPayment(@PathParam("transaction-id") UUID transactionId);

}
