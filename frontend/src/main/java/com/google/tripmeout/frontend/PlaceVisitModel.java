package com.google.tripmeout.frontend;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PlaceVisitModel {
  public static enum UserMark { YES, MAYBE, NO, UNKNOWN }

  public static Builder builder() {
    return new AutoValue_PlaceVisitModel.Builder();
  }

  public abstract String placeId();
  public abstract String name();
  public abstract String tripId();
  public abstract UserMark userMark();
  public abstract double latitude();
  public abstract double longitude();

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder setPlaceId(String placeId);
    public abstract Builder setName(String name);
    public abstract Builder setTripId(String tripId);
    public abstract Builder setUserMark(UserMark userMark);
    public abstract Builder setLatitude(double latitude);
    public abstract Builder setLongitude(double longitude);
    public abstract PlaceVisitModel build();
  }
}
