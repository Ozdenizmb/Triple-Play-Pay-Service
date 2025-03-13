package com.payment.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final Response response;

    public ApiException(Exception e) {
        super(e);
        Response r = null;
        if(e instanceof WebApplicationException) {
            r = ((WebApplicationException)e).getResponse();
        }
        this.response = r;
    }

    public ApiException(String message, Response response) {
        super(message);
        this.response = response;
    }

    public String getBody() {
        if(response == null) {
            return null;
        }

        try {
            return response.readEntity(String.class);
        } catch(Exception e) {
            return null;
        }
    }

}
