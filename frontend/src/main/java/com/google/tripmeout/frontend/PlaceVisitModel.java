package com.google.tripmeout.frontend;

import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
public abstract class PlaceVisitModel {
  public static enum UserMark { YES, MAYBE, NO, UNKNOWN }

  public static Builder builder() {
    return new AutoValue_PlaceVisitModel.Builder().setUserMark(UserMark.UNKNOWN);
  }

  public abstract String placeId();
  @Nullable public abstract String name();
  public abstract String tripId();
  public abstract UserMark userMark();
  @Nullable public abstract Double latitude();
  @Nullable public abstract Double longitude();

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder setPlaceId(String placeId);
    public abstract Builder setName(String name);
    public abstract Builder setTripId(String tripId);
    public abstract Builder setUserMark(UserMark userMark);
    public abstract Builder setLatitude(Double latitude);
    public abstract Builder setLongitude(Double longitude);
    public abstract PlaceVisitModel build();
  }
}
