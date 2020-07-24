package com.google.tripmeout.frontend;

import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
public abstract class TripModel {
  public static Builder builder() {
    return new AutoValue_TripModel.Builder();
  }

  @Nullable public abstract String id();
  @Nullable public abstract String userId();
  public abstract String name();
  public abstract String placesApiPlaceId();

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder setId(String id);
    public abstract Builder setUserId(String userId);
    public abstract Builder setName(String name);
    public abstract Builder setPlacesApiPlaceId(String placesApiPlaceId);
    public abstract TripModel build();
  }
}
