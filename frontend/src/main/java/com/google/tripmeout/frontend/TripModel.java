package com.google.tripmeout.frontend;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TripModel {
  public static Builder builder() {
    return new AutoValue_TripModel.Builder();
  }
  
  public abstract String id();
  public abstract String name();
  public abstract float locationLat();
  public abstract float locationLong();
  
  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder setId(String id);
    public abstract Builder setName(String name);
    public abstract Builder setLocationLat(float latitude);
    public abstract Builder setLocationLong(float longitude);
    public abstract TripModel build();
  }
}
