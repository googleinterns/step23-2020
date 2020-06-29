package com.google.tripmeout.frontend.error;

import java.lang.Exception;

public class TripAlreadyExistsException extends Exception{
  public TripAlreadyExistsException(String errorMssg) {
    super(errorMssg);
  }
}