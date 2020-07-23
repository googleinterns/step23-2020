
import 'package:test/test.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/rest_api_trip_service.dart';

void main(){
  group('Empty RestApiTripService',(){
    var tripService = TripService();
    test('gettrip returns Null', () async {
      expect(await tripService.getTrip('sometrip'), isNull);
    });
     test('listTrips returns empty list', () async {
      expect(await tripService.listTrips(), isEmpty);
    });
  });
  group('Populated RestApiTripService', () {
    var tripService = TripService();
    var trip1 = Trip(id: '123', name: 'trip1');
    var trip2 = Trip(id: '456', name: 'trip2');
    // do async setup inside a setUp lambda because group lambdas cant be async
    setUp(() async {
      await tripService.createTrip(trip1);
      await tripService.createTrip(trip2);
    });
    test('getTrip returns trip for existing id', () async {
      expect(await tripService.getTrip('456'), equals(trip2));
    });

    test('getTrip returns null for non-created id', () async {
      expect(await tripService.getTrip('789'), isNull);
    });

    test('listTrips returns all created trips', () async {
      expect(await tripService.listTrips(), unorderedEquals([trip1, trip2]));
    });
  });
}