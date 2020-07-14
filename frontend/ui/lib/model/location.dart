class Location {
  Location({this.longitude, this.latitude});

  final double longitude;
  final double latitude;

  static Location from(Location location, {longitude, latitude}) {
    return Location(
      longitude: longitude ?? location.longitude,
      latitude: latitude ?? location.latitude,
    );
  }
}
