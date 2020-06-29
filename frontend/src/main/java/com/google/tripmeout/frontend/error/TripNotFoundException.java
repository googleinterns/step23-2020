package com.google.tripmeout.frontend.error;

import java.lang.Exception;

public class TripNotFoundException extends Exception{
  public TripNotFoundException(String errorMssg) {
    super(errorMssg);
  }
}