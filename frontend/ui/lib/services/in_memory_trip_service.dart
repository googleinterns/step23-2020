import 'dart:async';
import 'dart:core';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/trip_service.dart';

class InMemoryTripService implements TripService {
  final Map<String, Trip> tripsById = Map();
  final Duration asyncWaitDuration;

  InMemoryTripService({asyncWaitDuration})
      : asyncWaitDuration = asyncWaitDuration ?? Duration();

  @override
  Future<List<Trip>> listTrips() async {
    return Future.delayed(asyncWaitDuration, () => tripsById.values.toList());
  }

  @override
  Future<Trip> getTrip(String id) async {
    return Future.delayed(asyncWaitDuration, () => tripsById[id]);
  }

  @override
  Future<Trip> createTrip(Trip trip) async {
    return Future.delayed(asyncWaitDuration, () => tripsById[trip.id] = trip);
  }
}
