import 'package:test/test.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/rest_api_trip_service.dart';

void main() {
  group('Populated RestApiTripService', () {
    var tripService = RestApiTripService(endpoint: 'http://localhost:8080');
    test('getTrip returns trip for existing id', () async {
      var trip = Trip(name: 'trip');
      var createdTrip = await tripService.createTrip(trip);
      expect(await tripService.getTrip(createdTrip.id), equals(trip));
    });

    test('getTrip returns null for non-created id', () async {
      var trip = Trip(name: 'trip');
      await tripService.createTrip(trip);
      expect(await tripService.getTrip('789'), isNull);
    });

    test('listTrips returns all created trips', () async {
      var trip = Trip(name: 'trip');
      await tripService.createTrip(trip);
      var trip2 = Trip(name: 'trip2');
      await tripService.createTrip(trip2);
      expect(await tripService.listTrips(), unorderedEquals([trip, trip2]));
    });
  });
}
