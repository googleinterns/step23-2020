import 'dart:async';
import 'package:google_maps/google_maps_places.dart';

class PlacesApiServices {
  Future<List<AutocompletePrediction>> getAutocomplete(
      String input, List<String> allowedTypes) {
    if (input == null || input == "") {
      return Future.sync(() => []);
    }

    Completer<List<AutocompletePrediction>> completer = Completer();
    AutocompletePrediction london = AutocompletePrediction()
      ..description = 'London, UK'
      ..placeId = 'LCY';

    AutocompletePrediction la = AutocompletePrediction()
      ..description = 'Los Angeles, CA, Us'
      ..placeId = 'LAX';

    completer.complete([london, la]);
    return completer.future;
  }

  Future<List<String>> getPhotos(String placeId) {
    Completer<List<String>> completer = Completer();
    completer.complete([]);
    return completer.future;
  }
}
