import 'package:flutter/material.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/trip_view_widget.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';
import 'package:tripmeout/widgets/routing_button_widget.dart';
import 'package:tripmeout/router/router.dart';

class TripViewPage extends StatelessWidget {
  final TripService tripService;
  final PlaceVisitService placeVisitService;
  final PlacesApiServices placesApiServices;
  final String tripId;

  TripViewPage(this.tripService, this.placeVisitService, this.placesApiServices,
      this.tripId,
      {Key key})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: defaultAppBar(context),
      body: Center(
          child: TripViewWidgetFromService(this.tripService,
              this.placeVisitService, this.placesApiServices, this.tripId)),
      floatingActionButton: FloatingRoutingButton(
        Router.createCreatePlaceVisitRoute(tripId),
        Icon(Icons.add),
        "Click to add a place to your trip.",
      ),
    );
  }
}
