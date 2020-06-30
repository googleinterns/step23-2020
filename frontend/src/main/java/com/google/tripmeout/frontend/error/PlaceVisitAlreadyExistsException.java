package com.google.tripmeout.frontend.error;

import com.google.tripmeout.frontend.error.TripMeOutException;
import java.lang.Throwable;
import javax.servlet.http.HttpServletResponse;


/**
 * exception to throw if a PlaceVisitModel object already exists in storage
 */
public class PlaceVisitAlreadyExistsException extends TripMeOutException {
  public PlaceVisitAlreadyExistsException(String errorMsg) {
    super(errorMsg);
  }

  public PlaceVisitAlreadyExistsException(String errorMsg, Throwable cause) {
    super(errorMsg, cause);
  }

  @Override
  public int restStatusCode() {
    return HttpServletResponse.SC_CONFLICT;
  }
}
