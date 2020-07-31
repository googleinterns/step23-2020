package com.google.tripmeout.frontend;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AuthenticationInfoModel {
  public static Builder builder() {
    return new AutoValue_AuthenticationInfoModel.Builder();
  }

  public abstract String returnLink();
  public abstract boolean loggedIn();

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract String returnLink(String returnLink);
    public abstract boolean loggedIn(boolean loggedIn);
    public abstract PlaceVisitModel build();
  }
}