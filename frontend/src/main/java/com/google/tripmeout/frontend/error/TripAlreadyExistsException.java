package com.google.tripmeout.frontend.error;

import com.google.tripmeout.frontend.error.TripMeOutException;
import java.lang.Throwable;
import javax.servlet.http.HttpServletResponse;


/**
 * exception to throw if a TripModel object already exists in storage
 */
public class TripAlreadyExistsException extends TripMeOutException {
  public TripAlreadyExistsException(String errorMssg) {
    super(errorMssg);
  }

  public TripAlreadyExistsException(String errorMssg, Throwable cause) {
    super(errorMssg, cause);
  }

  @Override
  public int restStatusCode() {
    return HttpServletResponse.SC_CONFLICT;
  }
}
