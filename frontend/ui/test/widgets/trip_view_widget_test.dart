import 'dart:async';

import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/trip_view_widget.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';

class MockTripService extends Mock implements TripService {}

void main() {
  testWidgets('Showing the text on page correctly shows up',
      (WidgetTester tester) async {
    var tripService = MockTripService();
    when(tripService.getTrip(any))
        .thenAnswer((_) => Future.value(Trip(id: 'id1', name: 'name1')));

    var tripViewWidget = TripViewWidgetFromService(tripService, 'id1');

    await tester.pumpWidget(wrapForDirectionality(tripViewWidget));
    await tester.pumpAndSettle();

    // Should be everything on the view trip screen expanded...
    expect(find.text("name1"), findsOneWidget);
    expect(find.text("Place 1"), findsOneWidget);
    expect(find.text("Place 2"), findsOneWidget);
    expect(find.text("Place 3"), findsOneWidget);
    expect(find.text("Place 4"), findsOneWidget);
    expect(find.text("Place 5"), findsOneWidget);
  });

  testWidgets('Showing the expanded Place correctly shows up',
      (WidgetTester tester) async {
    var tripService = MockTripService();
    when(tripService.getTrip(any))
        .thenAnswer((_) => Future.value(Trip(id: 'id1', name: 'name1')));

    var tripViewWidget = TripViewWidgetFromService(tripService, 'id1');
    await tester.pumpWidget(wrapForDirectionality(tripViewWidget));
    await tester.pumpAndSettle();

    expect(find.text("name1"), findsOneWidget);
    expect(find.text("Place 1"), findsOneWidget);
    expect(find.text("Description"), findsNothing);
    expect(find.text("Foo"), findsNothing);
    expect(find.text("Bar"), findsNothing);
    expect(find.text("Baz"), findsNothing);

    await tester.tap(find.widgetWithText(ExpansionTile, "Place 1"));
    await tester.pumpAndSettle();

    //Shows expanded Place
    expect(find.text("name1"), findsOneWidget);
    expect(find.text("Place 1"), findsOneWidget);
    expect(find.text("Description"), findsOneWidget);
    expect(find.text("Foo"), findsOneWidget);
    expect(find.text("Bar"), findsOneWidget);
    expect(find.text("Baz"), findsOneWidget);
  });

  testWidgets('Showing the each place has an M T and Delete',
      (WidgetTester tester) async {
    var placeBlockWidget = PlaceBlockWidget("Hello World");
    await tester.pumpWidget(wrapForDirectionality(placeBlockWidget));
    await tester.pumpAndSettle();

    expect(find.text("Hello World"), findsOneWidget);
    expect(find.byIcon(Icons.favorite), findsOneWidget);
    expect(find.byIcon(Icons.alarm), findsOneWidget);
    expect(find.byType(IconButton), findsOneWidget);
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
