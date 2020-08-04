import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/widgets/create_place_visit_widget.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/model/place_visit.dart';

class MockPlaceVisitService extends Mock implements PlaceVisitService {}

class MockPlacesApiService extends Mock implements PlacesApiServices {}

void main() {
  var placeVisitService = MockPlaceVisitService();
  var placesApiServices = PlacesApiServices();
  testWidgets('Showing the text on page correctly shows up',
      (WidgetTester tester) async {
    var createTripsWidget =
        CreatePlaceVisitWidget(placeVisitService, placesApiServices, "abc123");
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    // Should be everything on the create trip screen...
    expect(find.text('Enter your Destination'), findsOneWidget);
    expect(find.byType(IconButton), findsNWidgets(2));
    expect(find.widgetWithIcon(IconButton, Icons.send), findsOneWidget);
  });

  testWidgets(
      'Testing the submit button which wont create place visit if list tile is not selected',
      (WidgetTester tester) async {
    when(placeVisitService.createPlaceVisit(any))
        .thenAnswer((t) => Future.value(t.positionalArguments[0]));

    var createPlaceVisitWidget =
        CreatePlaceVisitWidget(placeVisitService, placesApiServices, 'abc123');

    await tester.pumpWidget(wrapForDirectionality(createPlaceVisitWidget));
    await tester.pumpAndSettle();

    await tester.tap(find.widgetWithIcon(IconButton, Icons.send));
    await tester.pumpAndSettle();

    verifyNever(placeVisitService.createPlaceVisit(captureAny));
  });

  testWidgets('Testing the submit button which creates a place visit',
      (WidgetTester tester) async {
    when(placeVisitService.createPlaceVisit(any))
        .thenAnswer((t) => Future.value(t.positionalArguments[0]..id = '123'));

    var createPlaceVisitWidget =
        CreatePlaceVisitWidget(placeVisitService, placesApiServices, 'abc123');

    await tester.pumpWidget(wrapForDirectionality(createPlaceVisitWidget));
    await tester.pumpAndSettle();

    await tester.enterText(
        find.widgetWithText(TextField, 'Enter your Destination'), 'London');
    await tester.pumpAndSettle(Duration(seconds: 2));

    expect(find.byType(ListTile), findsNWidgets(2));

    await tester.tap(find.widgetWithText(ListTile, 'London, UK'));
    await tester.pumpAndSettle();

    await tester.tap(find.widgetWithIcon(IconButton, Icons.send));
    await tester.pumpAndSettle(Duration(seconds: 2));

    PlaceVisit createdPlaceVisit =
        verify(placeVisitService.createPlaceVisit(captureAny)).captured.single;

    expect(createdPlaceVisit.name, equals('London, UK'));
    expect(createdPlaceVisit.placesApiPlaceId, equals('LCY'));
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
