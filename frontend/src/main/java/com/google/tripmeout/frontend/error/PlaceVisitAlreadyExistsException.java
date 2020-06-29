package com.google.tripmeout.frontend.error;

import java.lang.Exception;

public class PlaceVisitAlreadyExistsException extends Exception{
  public PlaceVisitAlreadyExistsException(String errorMssg) {
    super(errorMssg);
  }
}