import 'dart:async';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/widgets/trip_list_widget.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/trip_service.dart';

class MockTripService extends Mock implements TripService {}

void main() {
  testWidgets('transitions from waiting to listview when listTrips completes',
      (WidgetTester tester) async {
    var tripService = MockTripService();
    var listCompleter = Completer<List<Trip>>();
    when(tripService.listTrips()).thenAnswer((_) => listCompleter.future);
    var tripListWidget = ServiceLoadedTripListWidget(tripService);

    await tester.pumpWidget(wrapForDirectionality(tripListWidget));

    // Should be in loading screen...
    expect(find.text('name1'), findsNothing);
    expect(find.text('name2'), findsNothing);
    expect(find.byType(CircularProgressIndicator), findsOneWidget);

    listCompleter.complete(
        [Trip(id: 'id1', name: 'name1'), Trip(id: 'id2', name: 'name2')]);

    await tester.pumpAndSettle();

    // Should be in the list screen...
    expect(find.text('name1'), findsOneWidget);
    expect(find.text('name2'), findsOneWidget);
    expect(find.byType(CircularProgressIndicator), findsNothing);
  });

  testWidgets('transitions from waiting to alert when listTrips fails',
      (WidgetTester tester) async {
    var tripService = MockTripService();
    var listCompleter = Completer<List<Trip>>();
    when(tripService.listTrips()).thenAnswer((_) => listCompleter.future);
    var tripListWidget = ServiceLoadedTripListWidget(tripService);

    await tester.pumpWidget(wrapForDirectionality(tripListWidget));

    // Should be in loading screen...
    expect(find.text('name1'), findsNothing);
    expect(find.text('name2'), findsNothing);
    expect(find.byType(CircularProgressIndicator), findsOneWidget);

    listCompleter.completeError(Exception('this some bs'));

    await tester.pumpAndSettle();

    // Should be in the list screen...
    expect(find.text('Error fetching trips'), findsOneWidget);
    expect(find.byType(CircularProgressIndicator), findsNothing);
  });

  testWidgets('transitions from trip list page to create trip page on click',
      (WidgetTester tester) async {
    var tripService = MockTripService();
    var listCompleter = Completer<List<Trip>>();
    when(tripService.listTrips()).thenAnswer((_) => listCompleter.future);
    var tripListWidget = ServiceLoadedTripListWidget(tripService);

    await tester.pumpWidget(wrapForDirectionality(tripListWidget));
    listCompleter.complete(
        [Trip(id: 'id1', name: 'name1'), Trip(id: 'id2', name: 'name2')]);

    await tester.pumpAndSettle();

    // Should be trip list page

    expect(find.byIcon(Icons.add), findsOneWidget);
    expect(find.text('name1'), findsOneWidget);
    expect(find.text('name2'), findsOneWidget);

    //Create trip page which should not be yet seen
    expect(find.text('Enter your Location'), findsNothing);
    expect(find.text('Radius KM'), findsNothing);
    expect(find.byType(MapWidget), findsNothing);
    expect(find.text('Grabbed info placed here.'), findsNothing);
    xpect(find.byType(RaisedButton), findsNothing);

    RaisedButton button =
        find.byType(CircularProgressIndicator).evaluate().first.widget;
    button.onPressed();

    // Should be going the create trip page
    await tester.pump();

    // Should be off of the trip list page
    expect(find.byIcon(Icons.add), findsNothing);
    expect(find.text('name1'), findsNothing);
    expect(find.text('name2777'), findsNothing);

    //Should now be on create trip page
    expect(find.text('Enter your Location'), findsOneWidget);
    expect(find.text('Radius KM'), findsOneWidget);
    expect(find.byType(MapWidget), findsOneWidget);
    expect(find.text('Grabbed info placed here.'), findsOneWidget);
    xpect(find.byType(RaisedButton), findsOneWidget);
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
