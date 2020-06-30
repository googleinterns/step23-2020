package com.google.tripmeout.frontend.error;

import com.google.tripmeout.frontend.error.TripMeOutException;
import java.lang.Throwable;
import javax.servlet.http.HttpServletResponse;

public class PlaceVisitNotFoundException extends TripMeOutException {
  public PlaceVisitNotFoundException(String errorMssg) {
    super(errorMssg);
  }

  public PlaceVisitNotFoundException(String errorMssg, Throwable cause) {
    super(errorMssg, cause);
  }

  @Override
  public int restStatusCode() {
    return HttpServletResponse.SC_NOT_FOUND;
  }
}