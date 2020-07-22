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
    var trip1 = Trip(id: 'id1', name: 'name1');
    var trip2 = Trip(id: 'id2', name: 'name2');
    // do async setup inside a setUp lambda because group lambdas cant be async
    setUp(() async {
      await tripService.createTrip(trip1);
      await tripService.createTrip(trip2);
    });
    test('getTrip returns trip for existing id', () async {
      expect(await tripService.getTrip('id1'), equals(trip1));
    });

    test('getTrip returns null for non-created id', () async {
      expect(await tripService.getTrip('id3'), isNull);
    });

    test('listTrips returns all created trips', () async {
      expect(await tripService.listTrips(), unorderedEquals([trip1, trip2]));
    });
  });
}
