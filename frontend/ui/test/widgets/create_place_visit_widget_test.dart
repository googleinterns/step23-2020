import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/widgets/create_place_visit_widget.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/model/place_visit.dart';

class MockPlaceVisitService extends Mock implements PlaceVisitService {}

void main() {
  testWidgets('Showing the text on page correctly shows up',
      (WidgetTester tester) async {
    var placeVisitService = MockPlaceVisitService();
    var placesApiServices = PlacesApiServices();
    var createTripsWidget =
        CreatePlaceVisitWidget(placeVisitService, placesApiServices, "abc123");
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    // Should be everything on the create trip screen...
    expect(find.text('Enter your Destination'), findsOneWidget);
    expect(find.byType(IconButton), findsNWidgets(2));
  });

  testWidgets('Testing the submit button which creates a place visit',
      (WidgetTester tester) async {
    var placeVisitService = MockPlaceVisitService();
    var placesApiServices = PlacesApiServices();

    when(placeVisitService.createPlaceVisit(any))
        .thenAnswer((t) => Future.value(t.positionalArguments[0]));

    var createPlaceVisitWidget =
        CreatePlaceVisitWidget(placeVisitService, placesApiServices, 'abc123');

    await tester.pumpWidget(wrapForDirectionality(createPlaceVisitWidget));
    await tester.pumpAndSettle();

    await tester.enterText(
        find.widgetWithText(TextField, 'Enter your Location'), "London");

    expect(find.byType(ListTile), findsNWidgets(2));

    await tester.tap(find.byType(IconButton));
    await tester.pumpAndSettle();

    PlaceVisit createdPlaceVisit =
        verify(placeVisitService.createPlaceVisit(captureAny)).captured.single;

    expect(createdPlaceVisit.name, equals('London'));
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
