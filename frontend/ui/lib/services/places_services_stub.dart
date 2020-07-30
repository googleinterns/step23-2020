import 'dart:async';
import 'package:tripmeout/model/place.dart';

class PlacesApiServices {
  Future<List<PlaceWrapper>> getAutocomplete(
      String input, List<String> allowedTypes) {
    if (input == null || input == "") {
      return Future.sync(() => []);
    }

    Completer<List<PlaceWrapper>> completer = Completer();
    PlaceWrapper london = PlaceWrapper(name: 'London, UK', placeId: 'LCY');

    PlaceWrapper la = PlaceWrapper(name: 'Los Angeles, CA, Us', placeId: 'LAX');

    completer.complete([london, la]);
    return completer.future;
  }

  Future<List<String>> getPhotos(String placeId) {
    Completer<List<String>> completer = Completer();
    completer.complete([]);
    return completer.future;
  }

  Future<PlaceResult> getPlaceDetails(String placeId) {
    Completer<PlaceResult> completer = Completer();
    PlaceResult london = PlaceResult()
      ..placeId = placeId
      ..name = 'London'
      ..formattedAddress = 'London, UK'
      ..priceLevel = 4
      ..rating = 4
      ..types = ['city', 'tourist attraction']
      ..url = 'london.uk';
    
    completer.complete(london);
    return completer.future;
  }
}
