package com.kosinskyi.ecom.registry.exception;

public class NotYetImplementedException extends ApplicationException {

  public static final String NOT_YET_IMPLEMENTED_MESSAGE =
      "This method is not implemented yet!";

  public NotYetImplementedException() {
    super(NOT_YET_IMPLEMENTED_MESSAGE);
  }
}
