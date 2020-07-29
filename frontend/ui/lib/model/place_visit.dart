import 'dart:core';

enum UserMark {
  YES,
  NO,
  MAYBE,
  UNKNOWN,
}

class PlaceVisit {
  static final Map<String, UserMark> stringToUserMark = {
    "YES": UserMark.YES,
    "NO": UserMark.NO,
    "MAYBE": UserMark.MAYBE,
    "UNKNOWN": UserMark.UNKNOWN,
  };

  static UserMark userMarkEnumFromString(String userMark) {
    UserMark mark = stringToUserMark[userMark];
    if (mark == null) {
      throw Exception("invalid user mark");
    }
    return mark;
  }

  static String userMarkToString(UserMark userMark) {
    final s = userMark.toString();
    final dotIndex = s.lastIndexOf(".");
    return s.substring(dotIndex + 1);
  }

  PlaceVisit(
      {this.name, this.id, this.tripid, this.placesApiPlaceId, this.userMark});

  final String name;
  final String id;
  final String tripid;
  final UserMark userMark;
  final String placesApiPlaceId;

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
        this.id == other.id &&
        this.tripid == other.tripid &&
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
