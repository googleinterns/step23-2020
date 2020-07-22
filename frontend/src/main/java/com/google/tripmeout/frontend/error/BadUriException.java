package com.google.tripmeout.frontend.error;

import com.google.tripmeout.frontend.error.TripMeOutException;
import java.lang.Throwable;
import javax.servlet.http.HttpServletResponse;

/**
 * exception to be thrown when uri does not match expected pattern
 */
public class BadUriException extends TripMeOutException {
  public BadUriException(String errorMsg) {
    super(errorMsg);
  }

  public BadUriException(String errorMsg, Throwable cause) {
    super(errorMsg, cause);
  }

  @Override
  public int restStatusCode() {
    // if uri does not match, this indicates that the request was sent to wrong
    // servlet, thus a server configuration error
    return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
  }
}
