import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/widgets/create_trip_widget.dart';
import 'package:tripmeout/widgets/map_widget.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/model/trip.dart';

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

  testWidgets('Testing the submit button which creates a trip',
      (WidgetTester tester) async {
    var tripService = MockTripService();

    when(tripService.createTrip(any))
        .thenAnswer((t) => Future.value(t.positionalArguments[0]));

    var createTripsWidget = CreateTripWidget(tripService);
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    await tester.enterText(
        find.widgetWithText(TextField, 'Enter your Location'), "Italy");
    await tester.enterText(find.widgetWithText(TextField, 'Radius KM'), "25");

    RaisedButton button =
        find.widgetWithText(RaisedButton, 'Submit').evaluate().first.widget;

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
