package com.google.tripmeout.frontend.error;

import java.lang.Exception;
import java.lang.Throwable;

public abstract class TripMeOutException extends Exception {
  public TripMeOutException(String errorMssg) {
    super(errorMssg);
  }

  public TripMeOutException(String errorMssg, Throwable cause) {
    super(errorMssg, cause);
  }

  public abstract int restStatusCode();
}