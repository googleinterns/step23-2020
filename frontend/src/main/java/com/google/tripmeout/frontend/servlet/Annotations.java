package com.google.tripmeout.frontend.servlet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;

class Annotations {
  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  @interface UserId {}

  private Annotations() {}
}
