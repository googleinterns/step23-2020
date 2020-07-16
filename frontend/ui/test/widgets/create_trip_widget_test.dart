import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:tripmeout/widgets/create_trip_widget.dart';
import 'package:tripmeout/widgets/map_widget.dart';

void main() {
  testWidgets('Showing the text on page correctly shows up',
      (WidgetTester tester) async {
    var createTripsWidget = CreateTripWidget();
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
    var createTripsWidget = CreateTripWidget();
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    expect(find.byType(RaisedButton), findsOneWidget);
    expect(find.text('Grabbed info placed here.'), findsOneWidget);
    expect(find.text('No Input 0 km'), findsNothing);
    RaisedButton button =
        find.widgetWithText(RaisedButton, 'Submit').evaluate().first.widget;
    button.onPressed();

    // Should be showing the new text after button push
    await tester.pump();

    //TODO: Remove this part of test after inserted text on screen is deleted
    expect(find.text('No Input 0 km'), findsOneWidget);
    expect(find.text('Grabbed info placed here.'), findsNothing);
  });

  testWidgets('Testing the text inputs', (WidgetTester tester) async {
    //TODO: Remove this test after inserted text on screen is deleted
    var createTripsWidget = CreateTripWidget();
    await tester.pumpWidget(wrapForDirectionality(createTripsWidget));
    await tester.pumpAndSettle();

    expect(find.byType(RaisedButton), findsOneWidget);
    expect(find.text('Grabbed info placed here.'), findsOneWidget);
    expect(find.text('Italy 25 km'), findsNothing);

    RaisedButton button =
        find.widgetWithText(RaisedButton, 'Submit').evaluate().first.widget;

    await tester.enterText(
        find.widgetWithText(TextFormField, 'Enter your Location'), "Italy");
    await tester.enterText(
        find.widgetWithText(TextFormField, 'Radius KM'), "25");

    button.onPressed();

    // The inserted text should now appear on the screen
    await tester.pump();
    expect(find.text('Italy 25 km'), findsOneWidget);
    expect(find.text('Grabbed info placed here.'), findsNothing);
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
