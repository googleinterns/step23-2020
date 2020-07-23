package com.google.tripmeout.frontend.error;

import com.google.tripmeout.frontend.error.TripMeOutException;
import java.lang.Throwable;
import javax.servlet.http.HttpServletResponse;

/**
 * error to be thrown when given placesApiPlaceId is not a valid id according
 * to the Places API
 */
public class InvalidPlaceIdException extends TripMeOutException {
  public InvalidPlaceIdException(String errorMsg) {
    super(errorMsg);
  }

  public InvalidPlaceIdException(String errorMsg, Throwable cause) {
    super(errorMsg, cause);
  }

  @Override
  public int restStatusCode() {
    return HttpServletResponse.SC_BAD_REQUEST;
  }
}
