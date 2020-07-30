import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_typeahead/flutter_typeahead.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/widgets/autocomplete_text_field_widget.dart';
import 'package:tripmeout/model/place_visit.dart';

import 'create_trip_widget_test.dart';

void main() {
  PlacesApiServices placesApiServices = PlacesApiServices();
  testWidgets('Showing the text on page correctly shows up',
      (WidgetTester tester) async {
    var autocompleteWidget =
        MapsApiPlacesTextFieldWidget(['(cities)'], placesApiServices);
    await tester.pumpWidget(wrapForDirectionality(autocompleteWidget));

    await tester.pumpAndSettle();

    expect(find.text('Enter your Destination'), findsOneWidget);
    expect(find.text('Please enter a city'), findsOneWidget);
    expect(find.byType(ListTile), findsNothing);
  });

  testWidgets('Typing in text gives suggestions', (WidgetTester tester) async {
    var autocompleteWidget =
        MapsApiPlacesTextFieldWidget(['(cities)'], placesApiServices);
    await tester.pumpWidget(wrapForDirectionality(autocompleteWidget));

    Finder autocomplete =
        find.widgetWithText(TextField, 'Enter your Destination');
    expect(autocomplete, findsOneWidget);
    await tester.enterText(find.byType(TextField), "London");

    await tester.pumpAndSettle();
    expect(find.byType(ListTile), findsNWidgets(2));

    PlaceVisit london = PlaceVisit(placesApiPlaceId: "LCY", name: "London, UK");

    await tester.tap(find.widgetWithText(ListTile, 'London, UK'));
    await tester.pumpAndSettle();

    TextField textbox = autocomplete.evaluate().first.widget;
    expect(textbox.controller.text, "London, UK");
  });
}
