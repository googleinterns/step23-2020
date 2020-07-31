import 'dart:io';
import 'package:flutter_test/flutter_test.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/rest_api_trip_service.dart';

void main() {
  final serverRunning = restServerRunning();
  if (!serverRunning) {
    print(
        "Skipping REST API tests. Run tests with REST_SERVER_RUNNING=true to enable these.");
    return;
  }

  group('Populated RestApiTripService', () {
    var tripService = RestApiTripService(endpoint: 'http://localhost:8080');

    test('listTrips returns all created trips', () async {
      var trip = Trip(name: 'trip', placesApiPlaceId: 'abc123');
      var createdTrip1 = await tripService.createTrip(trip);
      var trip2 = Trip(name: 'trip2', placesApiPlaceId: 'abc123');
      var createdTrip2 = await tripService.createTrip(trip2);
      expect(await tripService.listTrips(), unorderedEquals([createdTrip1, createdTrip2]));
    });
    
    test('getTrip returns trip for existing id', () async {
      var trip = Trip(name: 'trip', placesApiPlaceId: 'abc123');
      var createdTrip = await tripService.createTrip(trip);
      expect(await tripService.getTrip(createdTrip.id), equals(createdTrip));
    });

    test('getTrip throws exception for non-created id', () async {
      var trip = Trip(name: 'trip', placesApiPlaceId: 'abc123');
      await tripService.createTrip(trip);
      expect(() async => await tripService.getTrip('789'), throwsException);
    });

    test('getTrip throws exception for deleted id', () async {
      var trip = Trip(name: 'trip', placesApiPlaceId: 'abc123');
      var createdTrip = await tripService.createTrip(trip);
      expect(await tripService.getTrip(createdTrip.id), equals(createdTrip));
      await tripService.deleteTrip(createdTrip.id);
      expect(() async => await tripService.getTrip(createdTrip.id), throwsException);
    });

    
  });
}

bool restServerRunning() {
  final runningValue = Platform.environment["REST_SERVER_RUNNING"] ?? "false";
  return runningValue.toLowerCase() == "true";
}
