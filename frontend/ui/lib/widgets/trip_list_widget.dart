import 'package:flutter/material.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/retryable_async_loadable.dart';

/// A TripListWidget that loads its data from a TripService.
class ServiceLoadedTripListWidget extends StatelessWidget {
  final TripService tripService;

  ServiceLoadedTripListWidget(this.tripService);

  @override
  Widget build(BuildContext context) {
    return RetryableAsyncLoadable(
      onLoad: (tripList) => TripListWidget(tripList),
      loadFunction: tripService.listTrips,
      errorMessage: "Error fetching trips",
    );
  }
}

/// A Widget for listing Trips.
class TripListWidget extends StatelessWidget {
  final List<Trip> trips;

  TripListWidget(this.trips);

  @override
  Widget build(BuildContext context) {
    return ListView(
      children: trips.map(toListTile).toList(),
    );
  }

  Widget toListTile(Trip trip) {
    return ListTile(
      title: Text(trip.name),
    );
  }
}
