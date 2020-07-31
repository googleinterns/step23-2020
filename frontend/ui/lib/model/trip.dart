import 'dart:core';

class Trip {
  Trip({this.name, this.id, this.userId, this.placesApiPlaceId});
  final String name;
  final String id;
  final String placesApiPlaceId;
  final String userId;

  factory Trip.from(Trip trip, {name, id, userId, placesApiPlaceId}) {
    return trip == null
        ? Trip(name: name, id: id, placesApiPlaceId: placesApiPlaceId)
        : Trip(
            name: name ?? trip.name,
            id: id ?? trip.id,
            userId: userId ?? trip.userId,
            placesApiPlaceId: placesApiPlaceId ?? trip.placesApiPlaceId,
          );
  }

  @override
  bool operator ==(other) {
    if (identical(this, other)) {
      return true;
    }
    return other is Trip &&
        this.name == other.name &&
        this.id == other.id &&
        this.userId == other.userId &&
        this.placesApiPlaceId == other.placesApiPlaceId;
  }

  @override
  int get hashCode {
    // This is bad, but dart gets upset if you don't have hashCode when you have equals.

    return this.name.hashCode ^
        this.id.hashCode ^
        this.placesApiPlaceId.hashCode;
  }

  factory Trip.fromJson(Map<String, dynamic> json) {
    return Trip(
        name: json['name'],
        id: json['id'],
        userId: json['userId'],
        placesApiPlaceId: json['placesApiPlaceId']);
  }
}
