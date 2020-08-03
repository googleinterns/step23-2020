import 'package:flutter/material.dart';
import 'package:tripmeout/services/places_services_html.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/recommended_widget.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';

class RecommendedPage extends StatelessWidget {
  final TripService tripService;
  final PlacesApiServices placesApiServices;
  final String tripId;

  RecommendedPage(this.tripService, this.placesApiServices, this.tripId, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: defaultAppBar(context),
      body: Center(
          child: RecommendedWidgetFromService(this.tripService, placesApiServices, this.tripId)),
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
