import 'package:flutter/material.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/trip_service.dart';

class TripListWidget extends StatefulWidget {
  final TripService tripService;

  TripListWidget(this.tripService);

  @override
  State createState() => _TripListWidgetState(tripService);
}

class _TripListWidgetState extends State<TripListWidget> {
  final TripService tripService;
  bool fetching = true;
  Exception error;
  List<Trip> fetchedTrips;

  _TripListWidgetState(this.tripService) {
    listTripsAndSetState();
  }

  // TODO: add a non-error refresh button?
  @override
  Widget build(BuildContext context) {
    if (fetching) {
      return Container(
        margin: const EdgeInsets.all(10.0),
        child: Padding(
          padding: const EdgeInsets.all(12.0),
          child: CircularProgressIndicator(),
        ),
      );
    }
    if (error != null) {
      return AlertDialog(
        title: Text('Error fetching trips'),
        content: Text('Would you like to retry?'),
        actions: <Widget>[
          FlatButton(
            child: Text('Retry'),
            onPressed: () {
              listTripsAndSetState();
            },
          ),
        ],
      );
    }
    return Container(
      margin: const EdgeInsets.all(10.0),
      child: _FetchedTripListWidget(fetchedTrips),
    );
  }

  void listTripsAndSetState() async {
    if (!fetching) {
      setState(() {
        this.fetching = true;
        this.error = null;
        this.fetchedTrips = null;
      });
    }
    try {
      var fetchedTrips = await tripService.listTrips();
      setState(() {
        this.fetching = false;
        this.error = null;
        this.fetchedTrips = fetchedTrips;
      });
    } catch (err) {
      setStateError(err);
    }
  }

  void setStateError(Exception err) {
    setState(() {
      this.fetching = false;
      this.error = err;
      this.fetchedTrips = null;
    });
  }
}

class _FetchedTripListWidget extends StatelessWidget {
  final List<Trip> trips;

  _FetchedTripListWidget(this.trips);

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
