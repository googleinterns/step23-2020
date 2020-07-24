import 'package:flutter/material.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/router/router.dart';
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
    var listItems = trips
        .map((trip) => ListTile(
              title: Text(trip.name),
              onTap: () {
                Navigator.pushNamed(
                    context, Router.createTripViewRoute(trip.id));
              },
            ))
        .toList();
    return ListView(children: listItems);
  }
}
