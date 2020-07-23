import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/widgets/create_trip_widget.dart';
import 'package:tripmeout/widgets/map_widget.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/services/in_memory_trip_service.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/model/location.dart';

class MockTripService extends Mock implements TripService {}

void main() {
  testWidgets('Showing the text on page correctly shows up',
      (WidgetTester tester) async {
    var tripService = MockTripService();
    var createTripsWidget = CreateTripWidget(tripService);
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    //TODO: Add test for loading screen after it's added to the Create Trip Widget

    await tester.pumpAndSettle();

    // Should be everything on the create trip screen...
    expect(find.text('Enter your Location'), findsOneWidget);
    expect(find.text('Radius KM'), findsOneWidget);
    expect(find.byType(MapWidget), findsOneWidget);
    expect(find.text('Grabbed info placed here.'), findsOneWidget);
    expect(find.byType(RaisedButton), findsOneWidget);
  });

  testWidgets('Testing the raised button', (WidgetTester tester) async {
    var tripService = MockTripService();
    var createTripsWidget = CreateTripWidget(tripService);
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    expect(find.byType(RaisedButton), findsOneWidget);
    expect(find.text('Grabbed info placed here.'), findsOneWidget);
    expect(find.text('No Input 0km'), findsNothing);
    RaisedButton button =
        find.widgetWithText(RaisedButton, 'Submit').evaluate().first.widget;
    button.onPressed();

    // Should be showing the new text after button push
    await tester.pump();

    //TODO: Remove this part of test after inserted text on screen is deleted
    expect(find.text('No Input 0km'), findsOneWidget);
    expect(find.text('Grabbed info placed here.'), findsNothing);
  });

  testWidgets('Testing the text inputs', (WidgetTester tester) async {
    //TODO: Remove this test after inserted text on screen is deleted
    var tripService = MockTripService();
    var createTripsWidget = CreateTripWidget(tripService);
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    expect(find.byType(RaisedButton), findsOneWidget);
    expect(find.text('Grabbed info placed here.'), findsOneWidget);
    expect(find.text('Italy 25km'), findsNothing);

    RaisedButton button =
        find.widgetWithText(RaisedButton, 'Submit').evaluate().first.widget;

    await tester.enterText(
        find.widgetWithText(TextField, 'Enter your Location'), "Italy");
    await tester.enterText(find.widgetWithText(TextField, 'Radius KM'), "25");

    button.onPressed();

    // The inserted text should now appear on the screen
    await tester.pump();
    expect(find.text('Italy 25km'), findsOneWidget);
    expect(find.text('Grabbed info placed here.'), findsNothing);
  });

  testWidgets('Testing the to assure no words are allowed in radius input',
      (WidgetTester tester) async {
    //TODO: Remove this test after inserted text on screen is deleted
    var tripService = MockTripService();
    var createTripsWidget = CreateTripWidget(tripService);
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    expect(find.byType(RaisedButton), findsOneWidget);
    expect(find.text('Grabbed info placed here.'), findsOneWidget);
    expect(find.text('AnyPlace km'), findsNothing);

    RaisedButton button =
        find.widgetWithText(RaisedButton, 'Submit').evaluate().first.widget;

    await tester.enterText(
        find.widgetWithText(TextField, 'Enter your Location'), "AnyPlace");
    await tester.enterText(
        find.widgetWithText(TextField, 'Radius KM'), "Not a Number");

    button.onPressed();

    // The inserted text should now appear on the screen
    await tester.pump();
    expect(find.text('AnyPlace 0km'), findsOneWidget);
    expect(find.text('Grabbed info placed here.'), findsNothing);
  });

  testWidgets('Testing the submit button which creates a trip',
      (WidgetTester tester) async {
    var tripService = MockTripService();

    when(tripService.createTrip(any))
        .thenAnswer((t) => Future.value(t.positionalArguments[0]));

    var createTripsWidget = CreateTripWidget(tripService);
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    RaisedButton button =
        find.widgetWithText(RaisedButton, 'Submit').evaluate().first.widget;

    await tester.enterText(
        find.widgetWithText(TextField, 'Enter your Location'), "Italy");
    await tester.enterText(find.widgetWithText(TextField, 'Radius KM'), "25");

    button.onPressed();
    await tester.pump();

    Trip createdTrip =
        verify(tripService.createTrip(captureAny)).captured.single;
    expect(createdTrip.name, equals('Italy'));
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
