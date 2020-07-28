import 'package:tripmeout/widgets/auto_complete_text_field_widget.dart';
import 'package:tripmeout/model/trip.dart';

class MockTripService extends Mock implements TripService {}

void main() {
  final AutocompleteService autocompleteService = AutocompleteService();
  final PlacesService placesService =
      PlacesService(document.getElementById("maps"));

  testWidgets('Showing the text on page correctly shows up',
      (WidgetTester tester) async {
    var autcompleteWidget = MapsApiPlacesTextFieldWidget(['(cities)'], placesService, autocompleteService);
    await tester.pumpWidget(wrapForDirectionality(autocompleteWidget));

    await tester.pumpAndSettle();

    // Should be everything on the create trip screen...
    expect(find.text('Enter your Destination'), findsOneWidget);
    expect(find.text('Please enter a city'), findsOneWidget);
    expect(find.byType(ListTile), findsNothing);
  });

  testWidgets('Typing in text gives suggestions', (WidgetTester tester) async {
    var autcompleteWidget = MapsApiPlacesTextFieldWidget(['(cities)']);

    TextField autocomplete = find.widgetWithText(TypeAheadField, 'Enter your Destination')
    await tester.enterText(autocomplete, "London");

    await tester.pumpAndSettle();
    expect(find.text('Enter your Destination'), findsOneWidget);
    expect(find.text('Please enter a city'), findsOneWidget);
    expect(find.byType(ListTile, findsOneWidget);

    ListTile suggestion =
        find.widgetWithText(ListTile, 'London, UK').evaluate().first.widget;

    suggestion.onSelected();
    await tester.pumpAndSettle();

    expect(autocomplete.controller.text, "London, UK");

  });
}