import 'package:flutter/material.dart';
import 'package:tripmeout/pages/create_trip_page.dart';
import 'package:tripmeout/pages/trip_list_page.dart';
import 'package:tripmeout/pages/trip_view_page.dart';
import 'package:tripmeout/router/router.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/services/in_memory_trip_service.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/model/location.dart';
import 'package:tripmeout/themes/default_theme.dart';
import 'package:tripmeout/router/router.dart';

void main() {
  runApp(TripMeOut());
}

class TripMeOut extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    //TODO: remove this...
    TripService tripService = InMemoryTripService();
    tripService.createTrip(Trip(
      id: 'fake-trip-id',
      name: 'My Fake Trip',
      location: Location(
        longitude: 0.0,
        latitude: 0.0,
      ),
    ));
    Router router = Router(tripService);
    return MaterialApp(
      title: 'Trip Me Out',
      theme: defaultTheme,
      onGenerateRoute: router.generateRoute,
      initialRoute: Router.tripListRoute,
    );
  }
}
