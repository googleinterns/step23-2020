package com.google.tripmeout.frontend.error;

import java.lang.Exception;

public class PlaceVisitNotFoundException extends Exception{
  public PlaceVisitNotFoundException(String errorMssg) {
    super(errorMssg);
  }
}