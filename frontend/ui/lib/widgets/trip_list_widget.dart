import 'package:flutter/material.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/router/router.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/alert_banner_widget.dart';
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
    if (trips.isEmpty) {
      var listItems = ListTile(
        leading: Icon(Icons.assignment_late, color: Colors.red),
        title: Center(
            child: Text(
                "You have yet to make a trip. Please click here or the \"New Trip\" button to make one.")),
        onTap: () {
          Navigator.pushNamed(context, Router.createTripRoute);
        },
      );
      return ListView(children: [listItems]);
    }
    var listItems = trips
        .map((trip) => ListTile(
              title: Text(trip.name),
              onTap: () {
                Navigator.pushNamed(
                    context, Router.createTripViewRoute(trip.id));
              },
              trailing: IconButton(
                onPressed: () {
                  _showDialog(context);
                },
                icon: Icon(Icons.delete),
                color: Colors.red,
                tooltip: 'Click here to delete trip.',
              ),
            ))
        .toList();
    return ListView(children: listItems);
  }

  _showDialog(BuildContext context) {
    VoidCallback continueCallBack = () => {
          Navigator.of(context).pop(),
          //I don't think we have a delete trip method.
        };
    AlertBannerWidget alert = AlertBannerWidget("Delete Trip",
        "Are you sure you would like to delete this trip?", continueCallBack);

    showDialog(
      context: context,
      builder: (BuildContext context) {
        return alert;
      },
    );
  }
}
