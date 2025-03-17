package com.payment.exception;

public class ErrorMessages {

    private ErrorMessages() {}

    public static final String DEFAULT_ERROR_MESSAGE = "An unexpected error occurred.";
    public static final String WRONG_TABLE_NAME = "Wrong table name.";
    public static final String WRONG_TRANSACTION_NAME = "Wrong transaction name.";
    public static final String BAD_REQUEST_TPP = "The request to the Triple Play Pay API was not successfully sent.";

}
