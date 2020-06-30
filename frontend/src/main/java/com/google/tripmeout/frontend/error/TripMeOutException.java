package com.google.tripmeout.frontend.error;

import java.lang.Exception;
import java.lang.Throwable;


/**
 * abstract class for custom exceptions in the TripMeOut REST API
 */
public abstract class TripMeOutException extends Exception {
  public TripMeOutException(String errorMssg) {
    super(errorMssg);
  }

  public TripMeOutException(String errorMssg, Throwable cause) {
    super(errorMssg, cause);
  }

  /**
   * returns HTTP response code associated with the exception thrown
   */
  public abstract int restStatusCode();
}
