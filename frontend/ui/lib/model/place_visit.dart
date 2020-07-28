import 'dart:core';

enum UserMark {
  YES, NO, MAYBE, UNKNOWN,
}

class PlaceVisit {

  UserMark userMarkEnumFromString (String userMark) {
    return UserMark.values.firstWhere((e) => e.toString() == "UserMark.$userMark");
  }

  PlaceVisit({this.name, this.id, this.tripid, this.placesApiPlaceId, this.userMark});
  
  final String name;
  final String id;
  final String tripid;
  final UserMark userMark;
  final String placesApiPlaceId;

  factory PlaceVisit.from(PlaceVisit placeVisit, {name, id, tripid, userMark, placesApiPlaceId}) {
    return placeVisit == null
        ? PlaceVisit(name: name, id: id, tripid: tripid, userMark: userMark, placesApiPlaceId: placesApiPlaceId)
        : PlaceVisit(
            name: name ?? placeVisit.name,
            id: id ?? placeVisit.id,
            tripid: tripid ?? placeVisit.tripid,
            userMark: userMark ?? placeVisit.userMark,
            placesApiPlaceId: placesApiPlaceId ?? placeVisit.placesApiPlaceId,
          );
  }

  @override
  bool operator ==(other) {
    if (identical(this, other)) {
      return true;
    }
    return other is PlaceVisit &&
        this.name == other.name &&
        this.id == other.id &&
        this.tripid == other.tripid &&
        this.userMark == other.userMark &&
        this.placesApiPlaceId == other.placesApiPlaceId;
  }

  @override
  int get hashCode {
    // This is bad, but dart gets upset if you don't have hashCode when you have equals.

    return this.name.hashCode ^
        this.id.hashCode ^
        this.placesApiPlaceId.hashCode;
  }

  factory PlaceVisit.fromJson(Map<String, dynamic> json) {
    return PlaceVisit(
        name: json['name'],
        id: json['id'],
        tripid: json['tripid'],
        userMark: userMarkEnumFromString(json['userMark']),
        placesApiPlaceId: json['placesApiPlaceId']);
  }
}