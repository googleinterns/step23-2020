package com.google.sps.tripmeout.frontend;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TripModel {
  public static Builder builder() {
    return new AutoValue_TripModel.Builder();
  }
  
  public abstract String id();
  public abstract String name();
  public abstract String userId();
  public abstract double locationLat();
  public abstract double locationLong();
  
  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder setId(String id);
    public abstract Builder setName(String name);
    public abstract Builder setUserId(String userId);
    public abstract Builder setLocationLat(double latitude);
    public abstract Builder setLocationLong(double longitude);
    public abstract TripModel build();
  }
}
