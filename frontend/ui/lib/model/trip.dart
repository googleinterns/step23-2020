import 'package:tripmeout/model/location.dart';

class Trip {
  Trip({this.name = "", this.id = "", location})
      : location = location ?? Location();

  final String name;
  final String id;
  final Location location;

  static Trip from(Trip trip, {name, id, location}) {
    return Trip(
      name: name ?? trip.name,
      id: id ?? trip.id,
      location: location ?? Location.from(trip.location),
    );
  }
}
