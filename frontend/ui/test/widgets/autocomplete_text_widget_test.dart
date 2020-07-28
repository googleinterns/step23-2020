import 'package:tripmeout/widgets/auto_complete_text_field_widget.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:google_maps/google_maps.dart';
import 'package:google_maps/google_maps_places.dart';

class MockPlacesApiService extends Mock implements PlacesApiServices {}

void main() {
  final AutocompleteService autocompleteService = MockAutocompleteService();
  final PlacesApiServices placesApiServices = MockPlacesService();

  testWidgets('Showing the text on page correctly shows up',
      (WidgetTester tester) async {
    var autcompleteWidget = MapsApiPlacesTextFieldWidget(['(cities)'], placesApiServices);
    await tester.pumpWidget(wrapForDirectionality(autocompleteWidget));

    await tester.pumpAndSettle();

    expect(find.text('Enter your Destination'), findsOneWidget);
    expect(find.text('Please enter a city'), findsOneWidget);
    expect(find.byType(ListTile), findsNothing);
  });

  testWidgets('Typing in text gives suggestions', (WidgetTester tester) async {
    var autcompleteWidget = MapsApiPlacesTextFieldWidget(['(cities)']);

    TextField autocomplete = find.widgetWithText(TypeAheadField, 'Enter your Destination')
    await tester.enterText(autocomplete, "London");

    AutocompletePrediction london = AutocompletePrediction()
      ..placeId = "LONDON,UK"
      ..description = "London, UK";

    when(autocompleteService.getPlacePredictions(any))
        .thenAnswer((_) => Future.value([london]));

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