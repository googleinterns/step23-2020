import 'dart:async';
import 'dart:core';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:uuid/uuid.dart';

class InMemoryTripService implements TripService {
  final Map<String, Trip> tripsById = Map();
  final Uuid uuid = Uuid();
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
    var tripWithUuid = Trip.from(trip, id: uuid.v4());
    return Future.delayed(asyncWaitDuration, () {
      tripsById[tripWithUuid.id] = tripWithUuid;
      return tripWithUuid;
    });
  }
}
