package com.google.sps.tripmeout.frontend;

import com.google.auto.value.AutoValue;

@AutoValue
public class TripModel {
  public abstract String id();
  public abstract String name();
  public abstract String location();

  public static Builder builder() {
    return new AutoValue_TripModel.Builder();
  }

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder setId(String id);
    public abstract Builder setName(String name);
    public abstract Builder setLocation(String location);
    public abstract TripModel build();
  }
}