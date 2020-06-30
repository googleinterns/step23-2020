package com.google.tripmeout.frontend.error;

import com.google.tripmeout.frontend.error.TripMeOutException;
import java.lang.Throwable;
import javax.servlet.http.HttpServletResponse;


/**
 * exception to throw if a PlaceVisitModel object already exists in storage
 */
public class PlaceVisitAlreadyExistsException extends TripMeOutException {
  public PlaceVisitAlreadyExistsException(String errorMssg) {
    super(errorMssg);
  }

  public PlaceVisitAlreadyExistsException(String errorMssg, Throwable cause) {
    super(errorMssg, cause);
  }

  @Override
  public int restStatusCode() {
    return HttpServletResponse.SC_CONFLICT;
  }
}
