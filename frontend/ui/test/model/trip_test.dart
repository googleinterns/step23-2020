import 'package:flutter_test/flutter_test.dart';
import 'package:tripmeout/model/trip.dart';

void main() {
  group('Trip.from', () {
    test('returns non-null for null trip', () {
      expect(Trip.from(null), isNotNull);
    });

    test('respects named params for null trip', () {
      Trip trip = Trip.from(null, name: 'some-name', id: 'some-id');
      expect(trip.name, equals('some-name'));
      expect(trip.id, equals('some-id'));
    });

    test('parameters default to input trip param', () {
      Trip original = Trip(name: 'some-name', id: 'some-id');
      expect(Trip.from(original), equals(original));
    });

    test('name parameter overrides input trip name', () {
      expect(
          Trip.from(Trip(name: 'name1'), name: 'name2').name, equals('name2'));
    });

    test('id parameter overrides input trip id', () {
      expect(Trip.from(Trip(id: 'id1'), id: 'id2').id, equals('id2'));
    });

    test('placesApiPlaceId parameter overrides input trip placesApiPlaceId',
        () {
      expect(
          Trip.from(Trip(placesApiPlaceId: 'placeId1'),
                  placesApiPlaceId: 'placeId2')
              .placesApiPlaceId,
          equals('placeId2'));
    });
  });
}
