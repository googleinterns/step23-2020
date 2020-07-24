import 'dart:core';
import 'package:tripmeout/model/location.dart';

class Trip {
  Trip({this.name, this.id, location}) : location = location ?? Location();

  final String name;
  final String id;
  final Location location;

  factory Trip.from(Trip trip, {name, id, location}) {
    return trip == null
        ? Trip(name: name, id: id, location: location)
        : Trip(
            name: name ?? trip.name,
            id: id ?? trip.id,
            location: location ?? Location.from(trip.location),
          );
  }

  @override
  bool operator ==(other) {
    if (identical(this, other)) {
      return true;
    }
    return other is Trip && this.name == other.name && this.id == other.id;
  }

  @override
  int get hashCode {
    // This is bad, but dart gets upset if you don't have hashCode when you have equals.
    return this.name.hashCode ^ this.id.hashCode;
  }
}
