import 'package:flutter/material.dart';
import 'package:tripmeout/pages/create_trip_page.dart';
import 'package:tripmeout/pages/log_in_page.dart';
import 'package:tripmeout/pages/create_place_visit_page.dart';
import 'package:tripmeout/pages/trip_list_page.dart';
import 'package:tripmeout/pages/trip_view_page.dart';
import 'package:tripmeout/services/login_service.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/services/places_services.dart';

class Router {
  static final String logInRoute = '/login';
  static final String tripListRoute = '/trips';
  static final String createTripRoute = '/trips/new';
  static final RegExp tripViewRouteRegExp = RegExp(r'/trips/(?<tripId>[^/]+)$');
  static final RegExp createPlaceVisitRouteRegExp =
      RegExp(r'/trips/(?<tripId>[^/]+)/placeVisits/new$');

  final TripService tripService;
  final PlaceVisitService placeVisitService;
  final LogInService logInService;
  final PlacesApiServices placesApiServices;

  static String createTripViewRoute(String tripId) {
    return "/trips/$tripId";
  }

  static String createCreatePlaceVisitRoute(String tripId) {
    return "/trips/$tripId/placeVisits/new";
  }

  static void routeToLoginPage(BuildContext context) {
    final currentRoute = ModalRoute.of(context).settings.name;
    Navigator.pushNamed(
      context,
      logInRoute,
      arguments: LogInPageArguments(currentRoute),
    );
  }

  Router(this.tripService, this.placeVisitService, this.logInService,
      this.placesApiServices);

  Route<dynamic> generateRoute(RouteSettings settings) {
    if (settings.name == tripListRoute) {
      return MaterialPageRoute(
        builder: (context) => TripListPage(tripService),
        settings: settings,
      );
    }
    if (settings.name == createTripRoute) {
      return MaterialPageRoute(
        builder: (context) => CreateTripPage(tripService, placesApiServices),
        settings: settings,
      );
    }
    if (settings.name == logInRoute) {
      return MaterialPageRoute(
        builder: (context) => LogInPage(
            logInService, settings.arguments ?? LogInPageArguments('/trips')),
        settings: settings,
      );
    }

    RegExpMatch match = tripViewRouteRegExp.firstMatch(settings.name);
    if (match != null) {
      String tripId = match.namedGroup('tripId');
      return MaterialPageRoute(
        builder: (context) => TripViewPage(
            tripService, placeVisitService, placesApiServices, tripId),
        settings: settings,
      );
    }

    match = createPlaceVisitRouteRegExp.firstMatch(settings.name);
    print(match);
    print("here");
    if (match != null) {
      String tripId = match.namedGroup('tripId');
      return MaterialPageRoute(
        builder: (context) =>
            CreatePlaceVisitPage(placeVisitService, placesApiServices, tripId),
        settings: settings,
      );
    }

    return MaterialPageRoute(
        builder: (_) => Scaffold(
              body:
                  Center(child: Text('No route defined for ${settings.name}')),
            ));
  }
}
