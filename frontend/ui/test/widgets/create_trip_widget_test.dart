import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/widgets/create_trip_widget.dart';
import 'package:tripmeout/widgets/map_widget.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/widgets/autocomplete_text_field_widget.dart';

class MockTripService extends Mock implements TripService {}

class MockPlacesApiServices extends Mock implements PlacesApiServices {}

void main() {
  final PlacesApiServices placesApiServices = MockPlacesApiServices();

  testWidgets('Showing the text on page correctly shows up',
      (WidgetTester tester) async {
    var tripService = MockTripService();
    var createTripsWidget = CreateTripWidget(tripService, placesApiServices);
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    //TODO: Add test for loading screen after it's added to the Create Trip Widget

    await tester.pumpAndSettle();

    // Should be everything on the create trip screen...
    expect(find.text('enter your trip name'), findsOneWidget);
    expect(find.text('Enter your Destination'), findsOneWidget);
    // TODO: Enable this when/if a MapWidget is added?
    // expect(find.byType(MapWidget), findsOneWidget);
    expect(find.byType(RaisedButton), findsOneWidget);
    expect(find.byType(MapsApiPlacesTextFieldWidget), findsOneWidget);
  });

  testWidgets('Testing the submit button which creates a trip',
      (WidgetTester tester) async {
    var tripService = MockTripService();

    when(tripService.createTrip(any))
        .thenAnswer((t) => Future.value(t.positionalArguments[0]));

    var createTripsWidget = CreateTripWidget(tripService, placesApiServices);
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    await tester.enterText(
        find.widgetWithText(TextField, 'enter your trip name'), "Italy");
    await tester.pumpAndSettle();

    await tester.tap(find.widgetWithText(RaisedButton, 'Submit'));
    await tester.pumpAndSettle();

    Trip createdTrip =
        verify(tripService.createTrip(captureAny)).captured.single;

    expect(createdTrip.name, equals('Italy'));
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
