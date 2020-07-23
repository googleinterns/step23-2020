package com.google.tripmeout.frontend.error;

import com.google.tripmeout.frontend.error.TripMeOutException;
import java.lang.Throwable;
import javax.servlet.http.HttpServletResponse;

/**
 * error to be thrown when an exception occurs when requesting details about
 * a placesApiPlaceId that is not an InvalidRequestException
 */
public class PlacesApiRequestException extends TripMeOutException {
  public PlacesApiRequestException(String errorMsg) {
    super(errorMsg);
  }

  public PlacesApiRequestException(String errorMsg, Throwable cause) {
    super(errorMsg, cause);
  }

  @Override
  public int restStatusCode() {
    return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
  }
}