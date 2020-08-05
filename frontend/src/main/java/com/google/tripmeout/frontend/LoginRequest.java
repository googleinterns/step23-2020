package com.google.tripmeout.frontend;

import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
public abstract class LoginRequest {
  @Nullable public abstract String redirectLink();

  public abstract Builder toBuilder();

  public static Builder builder() {
    return new AutoValue_LoginRequest.Builder();
  }

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder setRedirectLink(String redirectLink);
    public abstract LoginRequest build();
  }
}