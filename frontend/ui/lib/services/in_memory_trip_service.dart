import 'dart:async';
import 'dart:core';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/trip_service.dart';

class InMemoryTripService implements TripService {
  final Map<String, Trip> tripsById = Map();

  Future<List<Trip>> listTrips() async {
    return Future.delayed(
      Duration(seconds: 3),
      () => tripsById.values.toList(),
    );
  }

  Future<Trip> getTrip(String id) async {
    return Future.sync(() => tripsById[id]);
  }

  Future<Trip> createTrip(Trip trip) async {
    return Future.sync(() => tripsById[trip.id] = trip);
  }
}
