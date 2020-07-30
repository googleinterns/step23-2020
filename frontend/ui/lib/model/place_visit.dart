import 'dart:core';

enum UserMark {
  YES,
  NO,
  MAYBE,
  UNKNOWN,
}

class PlaceVisit {
  static final Map<String, UserMark> stringToUserMark = Map.fromIterable(
    UserMark.values,
    key: (v) {
      final s = v.toString();
      final dotIndex = s.lastIndexOf(".");
      return s.substring(dotIndex + 1);
    },
    value: (v) => v,
  );

  PlaceVisit(
      {this.name, this.id, this.tripid, this.placesApiPlaceId, this.userMark});

  final String name;
  final String id;
  final String tripid;
  UserMark userMark;
  final String placesApiPlaceId;

  static userMarkEnumFromString(String mark) {
    if (stringToUserMark[mark] == null) {
      //TODO: create custom exception
      throw Exception("invalid userMark");
    }
    return stringToUserMark[mark];
  }

  static String userMarkToString(UserMark userMark) {
    final s = userMark.toString();
    final dotIndex = s.lastIndexOf(".");
    return s.substring(dotIndex + 1);
  }

  factory PlaceVisit.from(PlaceVisit placeVisit,
      {name, id, tripid, userMark, placesApiPlaceId}) {
    return placeVisit == null
        ? PlaceVisit(
            name: name,
            id: id,
            tripid: tripid,
            userMark: userMark,
            placesApiPlaceId: placesApiPlaceId)
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
        name: json['placeName'],
        id: json['id'],
        tripid: json['tripId'],
        userMark: userMarkEnumFromString(json['userMark']),
        placesApiPlaceId: json['placesApiPlaceId']);
  }

  Map<String, dynamic> toJson() {
    Map<String, dynamic> json = Map();
    if (tripid != null) {
      json['tripId'] = this.tripid;
    }
    if (id != null) {
      json['id'] = this.id;
    }
    if (this.name != null) {
      json['placeName'] = this.name;
    }
    if (this.placesApiPlaceId != null) {
      json['placesApiPlaceId'] = this.placesApiPlaceId;
    }
    if (this.userMark != null) {
      json['userMark'] = PlaceVisit.userMarkToString(this.userMark);
    }

    return json;
  }
}
