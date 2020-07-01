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
    var tripListWidget = TripListWidget(tripService);

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
    var tripListWidget = TripListWidget(tripService);

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
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
