package com.kosinskyi.ecom.registry.error.exception;

public class NoDataFoundException extends ApplicationException {

  public NoDataFoundException(String message) {
    super(message);
  }

  public NoDataFoundException(Class<?> entityClass, Long id) {
    super(String.format("No %s found by id %d", entityClass.getSimpleName(), id));
  }
}
