package com.google.tripmeout.frontend;

import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
public abstract class TripModel {
  public static Builder builder() {
    return new AutoValue_TripModel.Builder();
  }

  public abstract String id();
  @Nullable public abstract String name();
  public abstract String userId();
  @Nullable public abstract Double locationLat();
  @Nullable public abstract Double locationLong();

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder setId(String id);
    public abstract Builder setName(String name);
    public abstract Builder setUserId(String userId);
    public abstract Builder setLocationLat(Double latitude);
    public abstract Builder setLocationLong(Double longitude);
    public abstract TripModel build();
  }
}
