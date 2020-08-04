import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/widgets/create_trip_widget.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/model/place.dart';
import 'package:tripmeout/widgets/autocomplete_text_field_widget.dart';

class MockTripService extends Mock implements TripService {}

class MockPlacesApiService extends Mock implements PlacesApiServices {}

void main() {
  PlacesApiServices placesApiServices = PlacesApiServices();
  testWidgets('Showing the text on page correctly shows up',
      (WidgetTester tester) async {
    var tripService = MockTripService();
    //PlacesApiServices placesApiServices = PlacesApiServices();
    var createTripsWidget = CreateTripWidget(tripService, placesApiServices);
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    // Should be everything on the create trip screen...
    expect(find.text('Enter your trip name'), findsOneWidget);
    expect(find.text('Enter your Destination'), findsOneWidget);
    expect(find.byType(IconButton), findsOneWidget);
    expect(find.byType(MapsApiPlacesTextFieldWidget), findsOneWidget);
  });

  testWidgets(
      'Testing the submit button does not submit without clicking autocomplete suggestion',
      (WidgetTester tester) async {
    var tripService = MockTripService();
    //PlacesApiServices placesApiServices = PlacesApiServices();

    when(tripService.createTrip(any))
        .thenAnswer((t) => Future.value(t.positionalArguments[0]));

    var createTripsWidget = CreateTripWidget(tripService, placesApiServices);
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    await tester.enterText(
        find.widgetWithText(TextField, 'Enter your trip name'), 'London Trip');
    await tester.pumpAndSettle();

    await tester.tap(find.byType(IconButton));
    await tester.pumpAndSettle();

    verifyNever(tripService.createTrip(captureAny));
  });

  testWidgets('Testing the submit button which creates a trip',
      (WidgetTester tester) async {
    var tripService = MockTripService();
    //var placesApiServices = MockPlacesApiService();

    when(tripService.createTrip(any))
        .thenAnswer((t) => Future.value(t.positionalArguments[0]..id = '123'));
    /*
    when(placesApiServices.getAutocomplete(any, any))
        .thenAnswer((t) => Future.value([PlaceWrapper(name: 'London, UK', placeId: 'abc123')]));

    when(placesApiServices.getPhotos(any))
        .thenAnswer((t) => Future.value([]));*/

    var createTripsWidget = CreateTripWidget(tripService, placesApiServices);
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    await tester.enterText(
        find.widgetWithText(TextField, 'Enter your trip name'), 'London Trip');
    await tester.pumpAndSettle();

    await tester.enterText(
        find.widgetWithText(TextField, 'Enter your Destination'), 'London');
    await tester.pumpAndSettle(Duration(seconds: 2));

    expect(find.byType(ListTile), findsNWidgets(2));

    await tester.tap(find.widgetWithText(ListTile, 'London, UK'));
    await tester.pumpAndSettle();

    await tester.tap(find.byType(IconButton));
    await tester.pumpAndSettle(Duration(seconds: 2));

    Trip createdTrip =
        verify(tripService.createTrip(captureAny)).captured.single;

    expect(createdTrip.name, equals('London Trip'));
    expect(createdTrip.placesApiPlaceId, equals('LCY'));
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
