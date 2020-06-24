package com.google.sps.tripmeout.frontend;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TripModel {
  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder setId(String id);
    public abstract Builder setName(String name);
    public abstract Builder setLocationLat(long latitude);
    public abstract Builder setLocationLong(long longitude);
    public abstract TripModel build();
  }
  
  public abstract String id();
  public abstract String name();
  public abstract long locationLat();
  public abstract long locationLong();

  public static Builder builder() {
    return new AutoValue_TripModel.Builder();
  }

  
}
