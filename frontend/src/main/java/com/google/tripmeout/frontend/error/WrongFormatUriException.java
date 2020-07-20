package com.google.tripmeout.frontend.error;

import com.google.tripmeout.frontend.error.TripMeOutException;
import java.lang.Throwable;
import javax.servlet.http.HttpServletResponse;

public class WrongFormatUriException extends TripMeOutException {
  public WrongFormatUriException(String errorMsg) {
    super(errorMsg);
  }

  public WrongFormatUriException(String errorMsg, Throwable cause) {
    super(errorMsg, cause);
  }

  @Override
  public int restStatusCode() {
    return HttpServletResponse.SC_BAD_REQUEST;
  }
}
