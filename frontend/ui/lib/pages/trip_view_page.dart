import 'package:flutter/material.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/trip_view_widget.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';

class TripViewPage extends StatelessWidget {
  final TripService tripService;
  final PlaceVisitService placeVisitService;
  final String tripId;

  TripViewPage(this.tripService, this.placeVisitService, this.tripId, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: defaultAppBar(context),
      body: Center(
          child: TripViewWidgetFromService(this.tripService, this.placeVisitService, this.tripId)),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          // TODO: Add place add method to this button
        },
        child: Icon(Icons.add),
        tooltip: "Click to add a place to your trip.",
      ),
    );
  }
}
