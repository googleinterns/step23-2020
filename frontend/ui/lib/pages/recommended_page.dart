import 'package:flutter/material.dart';
import 'package:tripmeout/services/places_services_html.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/recommended_widget.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/widgets/routing_button_widget.dart';
import 'package:tripmeout/router/router.dart';

class RecommendedPage extends StatelessWidget {
  final TripService tripService;
  final PlaceVisitService placeVisitService;
  final PlacesApiServices placesApiServices;
  final String tripId;

  RecommendedPage(this.tripService, this.placeVisitService, this.placesApiServices, this.tripId, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: defaultAppBar(context),
      body: Center(
          child: RecommendedWidgetFromService(this.tripService, this.placeVisitService, this.placesApiServices, this.tripId),
      ),
      floatingActionButton: FloatingRoutingButton(Router.createTripViewRoute(tripId), Icon(Icons.send), 'Click here to continue'),
    );
  }
}
