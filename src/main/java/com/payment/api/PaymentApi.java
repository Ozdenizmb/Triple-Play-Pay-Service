package com.payment.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/payments")
@Produces(MediaType.APPLICATION_JSON)
public interface PaymentApi {

    @GET
    @Path("/get-events")
    Response getEventsForPayment();

}
