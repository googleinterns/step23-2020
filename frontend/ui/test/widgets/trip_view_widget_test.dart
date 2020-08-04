import 'dart:async';

import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/trip_view_widget.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';

class MockTripService extends Mock implements TripService {}

class MockPlaceVisitService extends Mock implements PlaceVisitService {}

void main() {
  PlacesApiServices placesApiServices = PlacesApiServices();
  testWidgets('Showing the text on page correctly shows up',
      (WidgetTester tester) async {
    var tripService = MockTripService();
    var placeVisitService = MockPlaceVisitService();

    when(tripService.getTrip(any))
        .thenAnswer((_) => Future.value(Trip(id: 'id1', name: 'name1')));

    when(placeVisitService.listPlaceVisits(any)).thenAnswer(
        (_) => Future.value([PlaceVisit(id: 'id', name: 'Place1')]));

    var tripViewWidget = TripViewWidgetFromService(
        tripService, placeVisitService, placesApiServices, 'id1');

    await tester.pumpWidget(wrapForDirectionality(tripViewWidget));
    await tester.pumpAndSettle();

    // Should be everything on the view trip screen expanded...
    expect(find.text("name1"), findsOneWidget);
    expect(find.text("Place1"), findsOneWidget);
    expect(find.byType(PlaceBlockWidget), findsOneWidget);
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
