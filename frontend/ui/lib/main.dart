import 'package:flutter/material.dart';
import 'package:tripmeout/pages/trip_list_page.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/services/in_memory_trip_service.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/model/location.dart';

void main() {
  runApp(TripMeOut());
}

class TripMeOut extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    print('wait what');
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
    print('loading tripmeout');

    return MaterialApp(
      title: 'Trip Me Out',
      initialRoute: '/trips',
      onGenerateRoute: (settings) {
        // Add more pages here...
        if (settings.name == '/trips') {
          return MaterialPageRoute(
            builder: (context) => TripListPage(tripService),
            settings: settings,
          );
        }
        return null;
      },
    );
  }
}
