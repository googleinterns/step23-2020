import 'dart:core';


class PlaceWrapper {

  PlaceWrapper(
      {this.name, this.placeId, this.address, this.phoneNumber, this.website, this.rating, this.priceLevel, this.photos, this.types});

  final String name;
  final String placeId;
  final String address;
  final String phoneNumber;
  final String website;
  final num rating;
  final num priceLevel;
  final List<String> photos;
  final List<String> types;

}
