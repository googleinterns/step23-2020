class Location {
  Location({this.longitude, this.latitude});

  final double longitude;
  final double latitude;

  factory Location.from(Location location, {longitude, latitude}) {
    return location == null
        ? Location(latitude: latitude, longitude: longitude)
        : Location(
            longitude: longitude ?? location.longitude,
            latitude: latitude ?? location.latitude,
          );
  }
}
