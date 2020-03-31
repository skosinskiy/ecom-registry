package com.kosinskyi.ecom.registry.error.exception;

public class ApplicationException extends RuntimeException {

  public ApplicationException(String message) {
    super(message);
  }

  public ApplicationException(String message, Throwable exc) {
    super(message, exc);
  }
}
