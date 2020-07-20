import 'package:flutter/material.dart';
import 'package:tripmeout/pages/create_trip_page.dart';
import 'package:tripmeout/pages/trip_list_page.dart';
import 'package:tripmeout/pages/trip_view_page.dart';

cont String tripListRoute = '/trip';
cont String createTripRoute = '/createTrip';
cont String tripViewRoute = '/tripView';

class Routing{
  static Route<dynamic> generateRoute(RouteSettings settings){
    switch(settings.name){
      case tripListRoute:
        return MaterialPageRoute(
            builder: (context) => TripListPage(tripService),
            settings: settings,
            );
      case createTripRoute:
        return MaterialPageRoute(
            builder: (context) => CreateTripPage(),
            settings: settings,
          );
      case tripViewRoute:
        return MaterialPageRoute(
            builder: (context) => TripViewPage(),
            settings: settings,
          );
      default:
        return MaterialPageRoute(
            builder: (_) => Scaffold(
                  body: Center(
                      child: Text('No route defined for ${settings.name}')),
                ));
    }
  }
}