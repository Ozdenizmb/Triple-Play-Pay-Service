package com.payment.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import static com.payment.exception.ErrorMessages.DEFAULT_ERROR_MESSAGE;

@Getter
@Setter
public class ServiceException extends RuntimeException implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String message;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final String detail;
  @NotNull
  private final Response.Status status;

  protected ServiceException(Response.Status status, Throwable throwable) {
    super(throwable);
    this.status = status;
    this.message = throwable.getMessage();
    this.detail = (throwable.getMessage() == null || throwable.getMessage().isEmpty()) ? DEFAULT_ERROR_MESSAGE : throwable.getMessage();
  }

  protected ServiceException(Response.Status status, String message) {
    super(message);
    this.status = status;
    this.message = message;
    this.detail = null;
  }

  protected ServiceException(Response.Status status, String message, String errorDetail) {
    super(message);
    this.status = status;
    this.message = message;
    this.detail = errorDetail;
  }

  public static ServiceException withStatusAndThrowable(Response.Status status, Throwable throwable) {
    return new ServiceException(status, throwable);
  }

  public static ServiceException withStatusAndMessage(Response.Status status, String message) {
    return new ServiceException(status, message);
  }

  public static ServiceException withStatusAndDetails(Response.Status status, String message, String errorDetail) {
    return new ServiceException(status, message, errorDetail);
  }

}
