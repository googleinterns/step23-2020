package com.google.tripmeout.frontend.error;

import java.lang.Exception;
import java.lang.Throwable;

/**
 * abstract class for custom exceptions in the TripMeOut REST API
 */
public abstract class TripMeOutException extends Exception {
  public TripMeOutException(String errorMsg) {
    super(errorMsg);
  }

  public TripMeOutException(String errorMsg, Throwable cause) {
    super(errorMsg, cause);
  }

  /**
   * returns HTTP response code associated with the exception thrown
   */
  public abstract int restStatusCode();
}
