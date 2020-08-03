import 'package:tripmeout/model/trip.dart';

class TripService {
  Future<List<Trip>> listTrips() async {}
  Future<Trip> getTrip(String id) async {}
  Future<Trip> createTrip(Trip trip) async {}
  Future<void> deleteTrip(String id) async {}
}
