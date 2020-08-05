import 'package:flutter/material.dart';
import 'package:tripmeout/router/router.dart';
import 'package:tripmeout/services/login_service.dart';
import 'package:tripmeout/services/rest_api_login_service.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/services/rest_api_trip_service.dart';
import 'package:tripmeout/services/rest_api_place_visit_service.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/services/in_memory_trip_service.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/themes/default_theme.dart';

void main() {
  TripService tripService = RestApiTripService();
  PlaceVisitService placeVisitService = RestApiPlaceVisitService();
  LogInService logInService = RestApiLogInService();
  PlacesApiServices placesApiServices = PlacesApiServices();

  Router router = Router(tripService, placeVisitService, logInService, placesApiServices);
  runApp(TripMeOut(tripService, placeVisitService, router));
}

class TripMeOut extends StatelessWidget {
  TripService tripService;
  PlaceVisitService placeVisitService;
  Router router;

  TripMeOut(this.tripService, this.placeVisitService, this.router);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'TripMeOut',
      theme: defaultTheme,
      onGenerateRoute: router.generateRoute,
      initialRoute: Router.tripListRoute,
    );
  }
}
