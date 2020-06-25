package com.google.tripmeout.frontend;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PlaceVisitModel {
  public static Builder builder() {
    return new AutoValue_PlaceVisitModel.Builder();
  }

  public abstract String placeId();
  public abstract String name();
  public abstract String tripId();
  public abstract String userMark(); // must-see, if-time, don't-care, null
  public abstract float latitude();
  public abstract float logitude();

  @AutoValue.Builder
  public abstract class Builder {
    public abstract Builder setPlaceId(String placeId);
    public abstract Builder setName(String name);
    public abstract Builder setTripId(String tripId);
    public abstract Builder setUserMark(String userMark);
    public abstract Builder setLatitude(float latitude);
    public abstract Builder setLongitude(float longitude);
    public abstract PlaceVisitModel build();
  }
}