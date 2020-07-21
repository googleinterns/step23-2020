import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/widgets/link_button_widget.dart';
import 'package:tripmeout/widgets/map_widget.dart';
import 'package:tripmeout/services/trip_service.dart';

class MockTripService extends Mock implements TripService {}

void main() {
  testWidgets('transitions from trip list page to create trip page on click',
      (WidgetTester tester) async {
    var linkButtonWidget = LinkButton('/trips/new', Icon(Icons.add));

    await tester.pumpWidget(wrapForDirectionality(linkButtonWidget));
    await tester.pumpAndSettle();

    // Should be the button
    expect(find.byIcon(Icons.add), findsOneWidget);

    //Create trip page which should not be yet seen
    expect(find.text('Enter your Location'), findsNothing);
    expect(find.text('Radius KM'), findsNothing);
    expect(find.byType(MapWidget), findsNothing);
    expect(find.text('Grabbed info placed here.'), findsNothing);
    expect(find.byType(RaisedButton), findsNothing);

    RaisedButton button = find.byIcon(Icons.add).evaluate().first.widget;
    button.onPressed();

    // Should be going the create trip page
    await tester.pump();

    // Should be off of the trip list page
    expect(find.byIcon(Icons.add), findsOneWidget);

    expect(find.text('Enter your Location'), findsOneWidget);
    expect(find.text('Radius KM'), findsOneWidget);
    expect(find.byType(MapWidget), findsOneWidget);
    expect(find.text('Grabbed info placed here.'), findsOneWidget);
    expect(find.byType(RaisedButton), findsOneWidget);
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
