package com.google.tripmeout.frontend.error;

import com.google.tripmeout.frontend.error.TripMeOutException;
import java.lang.Throwable;
import javax.servlet.http.HttpServletResponse;

public class BadUriException extends TripMeOutException {
  public BadUriException(String errorMsg) {
    super(errorMsg);
  }

  public BadUriException(String errorMsg, Throwable cause) {
    super(errorMsg, cause);
  }

  @Override
  public int restStatusCode() {
    return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
  }
}
