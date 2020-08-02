import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/widgets/create_place_visit_widget.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/model/place_visit.dart';

class MockPlaceVisitService extends Mock implements PlaceVisitService {}

void main() {
  testWidgets('Showing the text on page correctly shows up',
      (WidgetTester tester) async {
    var placeVisitService = MockPlaceVisitService();
    var createTripsWidget = CreatePlaceVisitWidget(placeVisitService);
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    // Should be everything on the create trip screen...
    expect(find.text('Enter your Location'), findsOneWidget);
    expect(find.byType(ToggleButtons), findsOneWidget)
    // TODO: Enable this when/if a MapWidget is added?
    // expect(find.byType(MapWidget), findsOneWidget);
    expect(find.byType(RaisedButton), findsOneWidget);
  });

  testWidgets('Testing the submit button which creates a trip',
      (WidgetTester tester) async {
    var placeVisitService = MockPlaceVisitService();

    when(placeVisitService.createPlaceVisit(any))
        .thenAnswer((t) => Future.value(t.positionalArguments[0]));

    var createTripsWidget = CreatePlaceVisitWidget(placeVisitService, 'abc123');
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    // TODO: Consider enabling this after fixing the test/code. The display
    // behavior has changed since this was first introduced.

    await tester.enterText(
      find.widgetWithText(TextField, 'Enter your Location'), "London");

    RaisedButton button =
      find.widgetWithText(RaisedButton, 'Submit').evaluate().first.widget;

    button.onPressed();
    await tester.pump();

    PlaceVisit createdPlaceVisit =
       verify(placeVisitService.createPlaceVisit(captureAny)).captured.single;

    expect(createdPlaceVisit.name, equals('London'));
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}