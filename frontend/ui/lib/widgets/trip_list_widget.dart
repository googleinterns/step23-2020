import 'package:flutter/material.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/router/router.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/alert_banner_widget.dart';
import 'package:tripmeout/widgets/retryable_async_loadable.dart';
import 'package:tripmeout/widgets/trip_deleter_button.dart';

/// A TripListWidget that loads its data from a TripService.
class ServiceLoadedTripListWidget extends StatelessWidget {
  final TripService tripService;

  ServiceLoadedTripListWidget(this.tripService);

  @override
  Widget build(BuildContext context) {
    return RetryableAsyncLoadable(
      onLoad: (tripList) => TripListWidget(tripList, tripService),
      loadFunction: tripService.listTrips,
      errorMessage: "Error fetching trips",
    );
  }
}

/// A Widget for listing Trips.
class TripListWidget extends StatelessWidget {
  final List<Trip> trips;
  final TripService tripService;
  TripListWidget(this.trips, this.tripService);

  @override
  Widget build(BuildContext context) {
    if (trips.isEmpty) {
      var listItems = Card(
          child: ListTile(
        leading: Icon(Icons.assignment_late, color: Colors.red),
        title: Center(
            child: Text(
                "You have yet to make a trip. Please click here or the \"New Trip\" button to make one.")),
        onTap: () {
          Navigator.pushNamed(context, Router.createTripRoute);
        },
      ));
      return ListView(children: [listItems]);
    }
    var listItems = trips
        .map((trip) => Card(
                child: ListTile(
              title: Text(trip.name),
              onTap: () {
                Navigator.pushNamed(
                    context, Router.createTripViewRoute(trip.id));
              },
              trailing: TripDeleterButton(tripService, trip),
            )))
        .toList();
    return Column(children: [
      Card(
          child: Container(
              child: Center(
                  child: Text("List of Trips",
                      style: TextStyle(
                        fontSize: 30,
                      ))))),
      Expanded(child: ListView(children: listItems))
    ]);
  }
}
