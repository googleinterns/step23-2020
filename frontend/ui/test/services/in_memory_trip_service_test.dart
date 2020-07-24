import 'package:flutter_test/flutter_test.dart';
import 'package:tripmeout/services/in_memory_trip_service.dart';
import 'package:tripmeout/model/trip.dart';

void main() {
  group('Empty InMemoryTripService', () {
    var tripService = InMemoryTripService();
    test('getTrip returns null', () async {
      expect(await tripService.getTrip('lol'), isNull);
    });

    test('listTrips returns empty list', () async {
      expect(await tripService.listTrips(), isEmpty);
    });
  });

  group('Populated InMemoryTripService', () {
    var tripService = InMemoryTripService();
    var trip1 = Trip(name: 'name1');
    var trip2 = Trip(name: 'name2');
    // do async setup inside a setUp lambda because group lambdas cant be async
    setUpAll(() async {
      trip1 = await tripService.createTrip(trip1);
      trip2 = await tripService.createTrip(trip2);
    });
    test('getTrip returns trip for existing id', () async {
      expect(await tripService.getTrip(trip1.id), equals(trip1));
    });

    test('getTrip returns null for non-created id', () async {
      expect(await tripService.getTrip('unknown-id'), isNull);
    });

    test('listTrips returns all created trips', () async {
      expect(await tripService.listTrips(), unorderedEquals([trip1, trip2]));
    });
  });
}
