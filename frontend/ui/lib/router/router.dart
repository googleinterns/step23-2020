import 'package:flutter/material.dart';
import 'package:tripmeout/pages/create_trip_page.dart';
import 'package:tripmeout/pages/trip_list_page.dart';
import 'package:tripmeout/pages/trip_view_page.dart';
import 'package:tripmeout/services/trip_service.dart';

class Router {
  static final String tripListRoute = '/trips';
  static final String createTripRoute = '/trips/new';
  static final RegExp tripViewRouteRegExp = RegExp(r'/trips/(?<tripId>[^/]+)$');

  final TripService tripService;

  static String createTripViewRoute(String tripId) {
    return "/trips/$tripId";
  }

  Router(this.tripService);

  Route<dynamic> generateRoute(RouteSettings settings) {
    if (settings.name == tripListRoute) {
      return MaterialPageRoute(
        builder: (context) => TripListPage(tripService),
        settings: settings,
      );
    }
    if (settings.name == createTripRoute) {
      return MaterialPageRoute(
        builder: (context) => CreateTripPage(tripService),
        settings: settings,
      );
    }

    RegExpMatch match = tripViewRouteRegExp.firstMatch(settings.name);
    if (match != null) {
      String tripId = match.namedGroup('tripId');
      //TODO:inject actual tripId
      return MaterialPageRoute(
        builder: (context) => TripViewPage(tripService, tripId),
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
