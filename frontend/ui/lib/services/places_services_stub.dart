import 'dart:async';
import 'package:flutter/material.dart';
import 'package:tripmeout/model/place_visit.dart';


class PlacesApiServices {

  Future<List<PlaceVisit>> getAutocomplete(
      String input, List<String> allowedTypes) {
    if (input == null || input == "") {
      return Future.sync(() => []);
    }

    Completer<List<PlaceVisit>> completer = Completer();
    PlaceVisit london = PlaceVisit(name: 'London, UK', placesApiPlaceId: 'LCY');
    PlaceVisit la = PlaceVisit(name: 'Los Angeles, CA, US', placesApiPlaceId: 'LAX');
    completer.complete([london, la]);
    return completer.future;
  }

  Future<List<String>> getPhotos(String placeId) {
    Completer<List<String>> completer = Completer();
    completer.complete([]);
    return completer.future;
  }
}
