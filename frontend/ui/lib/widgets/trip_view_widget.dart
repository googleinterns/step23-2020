import 'package:flutter/material.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';

class TripViewWidgetFromService extends StatelessWidget {
  final TripService tripService;
  final String tripId;

  TripViewWidgetFromService(this.tripService, this.tripId);

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: tripService.getTrip(tripId),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return TripViewWidget(snapshot.data);
        }
        if (snapshot.hasError) {
          Scaffold.of(context).showSnackBar(SnackBar(
            content: Text("Error getting trip"),
            action: SnackBarAction(
              label: "Retry",
              onPressed: () {}, //TODO: Make retry button actually work.
            ),
          ));
          return Container();
        }
        return CircularProgressIndicator();
      },
    );
  }
}

class TripViewWidget extends StatelessWidget {
  final Trip trip;

  TripViewWidget(this.trip);

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
          border: Border.all(
            color: Colors.black,
          ),
          borderRadius: BorderRadius.all(Radius.circular(5))),
      margin: const EdgeInsets.all(15.0),
      padding: const EdgeInsets.all(10.0),
      child: Column(
        children: [
          Text(trip.name),
          PlaceBlockWidget("Place 1"),
          PlaceBlockWidget("Place 2"),
          PlaceBlockWidget("Place 3"),
          PlaceBlockWidget("Place 4"),
          PlaceBlockWidget("Place 5"),
        ],
      ),
    );
  }
}
