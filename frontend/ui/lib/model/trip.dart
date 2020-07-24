import 'dart:core';

class Trip {

  Trip({this.name, this.id, this.placesApiPlaceId});
  final String name;
  final String id;
  final String placesApiPlaceId;


  factory Trip.from(Trip trip, {name, id, placesApiPlaceId}) {

    return trip == null
        ? Trip(name: name, id: id, placesApiPlaceId: placesApiPlaceId)
        : Trip(
            name: name ?? trip.name,
            id: id ?? trip.id,
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
     placesApiPlaceId: json['placesApiPlaceId']
    );



      
  }
}
