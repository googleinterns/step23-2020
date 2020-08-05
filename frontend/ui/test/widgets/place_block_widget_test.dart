import 'dart:async';

import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/model/place.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/contact_info_widget.dart';
import 'package:tripmeout/widgets/trip_view_widget.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';
import 'package:tripmeout/widgets/user_status_widget.dart';
import 'package:tripmeout/widgets/place_details_widget.dart';

class MockTripService extends Mock implements TripService {}

class MockPlaceVisitService extends Mock implements PlaceVisitService {}

void main() {
  PlacesApiServices placesApiServices = PlacesApiServices();

  testWidgets('Showing the each place has correct widgets when not expanded',
      (WidgetTester tester) async {
    PlaceVisitService placeVisitService = MockPlaceVisitService();
    PlaceVisit placeVisit = PlaceVisit(id: 'id', name: 'Place1');
    PlaceWrapper placeWrapper = PlaceWrapper(
      address: 'blah',
      phoneNumber: '123',
      website: 'www.google.com',
      types: ['restaurant', 'hotel'],
      rating: 4.6,
      priceLevel: 3,
      photos: [],
    );

    var placeBlockWidget =
        PlaceBlockWidget(placeVisitService, placeVisit, placeWrapper);
    await tester.pumpWidget(wrapForDirectionality(placeBlockWidget));
    await tester.pumpAndSettle();

    expect(find.text("Place1"), findsOneWidget);
    expect(find.byType(UserStatusWidget), findsOneWidget);
    expect(find.byType(PlaceDetailsWidget), findsNothing);
  });

  testWidgets('showing that tapping on tile shows more info',
      (WidgetTester tester) async {
    PlaceVisitService placeVisitService = MockPlaceVisitService();
    PlaceVisit placeVisit = PlaceVisit(id: 'id', name: 'Place1');
    PlaceWrapper placeWrapper = PlaceWrapper(
      address: 'blah',
      phoneNumber: '123',
      website: 'www.google.com',
      types: ['restaurant', 'hotel'],
      rating: 4.6,
      priceLevel: 3,
      photos: [],
    );

    var placeBlockWidget =
        PlaceBlockWidget(placeVisitService, placeVisit, placeWrapper);
    await tester.pumpWidget(wrapForDirectionality(placeBlockWidget));
    await tester.pumpAndSettle();

    await tester.tap(find.byType(ExpansionTile));
    await tester.pumpAndSettle();

    expect(find.text("Place1"), findsOneWidget);
    expect(find.byType(UserStatusWidget), findsOneWidget);
    expect(find.byType(PlaceDetailsWidget), findsOneWidget);
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
