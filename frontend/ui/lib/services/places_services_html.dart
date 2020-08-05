import 'dart:async';
import 'dart:html';
import 'package:google_maps/google_maps.dart';
import 'package:google_maps/google_maps_places.dart';
import 'package:tripmeout/model/place.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/trip_service.dart';

class PlacesApiServices {
  final AutocompleteService autocompleteService = AutocompleteService();
  final PlacesService placesService =
      PlacesService(document.getElementById("maps"));

  Future<List<PlaceWrapper>> getAutocomplete(
      String input, List<String> allowedTypes) {
    if (input == null || input == "") {
      return Future.sync(() => []);
    }

    Completer<List<PlaceWrapper>> completer = Completer();
    AutocompletionRequest request = AutocompletionRequest()..input = input;

    if (allowedTypes.length > 0) {
      request = request..types = allowedTypes;
    }

    autocompleteService.getPlacePredictions(request, (result, status) {
      if (status == PlacesServiceStatus.OK) {
        completer.complete(result.map(autocompleteToPlaceWrapper).toList());
      } else if (status == PlacesServiceStatus.ZERO_RESULTS) {
        completer.complete([]);
      } else {
        completer.completeError(status);
      }
    });

    return completer.future;
  }

  Future<PlaceWrapper> getPlaceDetails(String placeId) {
    Completer<PlaceWrapper> completer = Completer();
    final request = PlaceDetailsRequest()..placeId = placeId;
    placesService.getDetails(request, (result, status) async {
      if (status == PlacesServiceStatus.OK) {
        completer.complete(placeResultToPlaceWrapper(result));
      } else {
        completer.completeError(status);
      }
    });
    return completer.future;
  }

  Future<List<String>> getPhotos(String placeId) {
    Completer<List<String>> completer = Completer();
    final request = PlaceDetailsRequest()..placeId = placeId;
    placesService.getDetails(request, (result, status) async {
      if (status == PlacesServiceStatus.OK) {
        final photoOptions = PhotoOptions()
          ..maxHeight = 500
          ..maxWidth = 500;

        List<String> images =
            result.photos.map((photo) => photo.getUrl(photoOptions));
        completer.complete(images);
      } else {
        completer.complete([]);
      }
    });
    return completer.future;
  }

  Future<List<PlaceWrapper>> getNearbyPlaces(
      TripService tripService, String tripId) async {
    Completer<List<PlaceWrapper>> completer = Completer();
    Trip trip = await tripService.getTrip(tripId);

    final detailsRequest = PlaceDetailsRequest()
      ..placeId = trip.placesApiPlaceId;
    placesService.getDetails(detailsRequest, (result, status) {
      if (status == PlacesServiceStatus.OK) {
        LatLng location = result.geometry.location;
        print(location);

        final request = PlaceSearchRequest()
          ..location = location
          ..radius = 50000
          ..types = ["tourist_attraction"];

        placesService.nearbySearch(request, (result, status, nextPage) {
          if (status == PlacesServiceStatus.OK) {
            completer.complete(result.map(placeResultToPlaceWrapper).toList());
          } else if (status == PlacesServiceStatus.ZERO_RESULTS) {
            completer.complete([]);
          } else {
            completer.completeError(status);
          }
        });
      } else {
        completer.completeError("could not find trip destination");
      }
    });

    return completer.future;
  }

  PlaceWrapper autocompleteToPlaceWrapper(AutocompletePrediction suggestion) {
    return PlaceWrapper(
        name: suggestion.description, placeId: suggestion.placeId);
  }

  PlaceWrapper placeResultToPlaceWrapper(PlaceResult nearby) {
    final photoOptions = PhotoOptions()
      ..maxHeight = 500
      ..maxWidth = 500;
    return PlaceWrapper(
      name: nearby.name,
      placeId: nearby.placeId,
      address: nearby.formattedAddress,
      phoneNumber: nearby.formattedPhoneNumber,
      website: nearby.website,
      priceLevel: nearby.priceLevel,
      rating: nearby.rating,
      types: nearby.types,
      photos: nearby.photos.map((photo) => photo.getUrl(photoOptions)).toList(),
    );
  }
}
