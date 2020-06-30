package com.google.tripmeout.frontend.error;

import com.google.tripmeout.frontend.error.TripMeOutException;
import java.lang.Throwable;
import javax.servlet.http.HttpServletResponse;

/**
 * exception to throw if a PlaceVisitModel object is not found in storage 
 */
public class PlaceVisitNotFoundException extends TripMeOutException {
  public PlaceVisitNotFoundException(String errorMsg) {
    super(errorMsg);
  }

  public PlaceVisitNotFoundException(String errorMsg, Throwable cause) {
    super(errorMsg, cause);
  }

  @Override
  public int restStatusCode() {
    return HttpServletResponse.SC_NOT_FOUND;
  }
}
