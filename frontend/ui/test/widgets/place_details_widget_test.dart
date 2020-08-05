import 'dart:async';

import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:mockito/mockito.dart';
import 'package:tripmeout/model/place.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/contact_info_widget.dart';
import 'package:tripmeout/widgets/trip_view_widget.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';
import 'package:tripmeout/widgets/user_status_widget.dart';
import 'package:tripmeout/widgets/place_details_widget.dart';

void main() {

  testWidgets('Showing that the widget has all the correct elements',
      (WidgetTester tester) async {
    PlaceWrapper placeWrapper = PlaceWrapper(
      address: 'blah',
      phoneNumber: '123',
      website: 'www.google.com',
      types: ['restaurant', 'hotel'],
      rating: 4.6,
      priceLevel: 3,
      photos: [],
    );

    var placeDetailsWidget =
        PlaceDetailsWidget(placeWrapper);
    await tester.pumpWidget(wrapForDirectionality(placeDetailsWidget));
    await tester.pumpAndSettle();

    expect(find.byType(ContactInfoWidget), findsOneWidget);
    expect(find.byIcon(Icons.star), findsNWidgets(4));
    expect(find.byIcon(Icons.star_border), findsNothing);
    expect(find.byIcon(Icons.star_half), findsOneWidget);
    expect(find.text('4.6'), findsOneWidget);
    expect(find.byIcon(Icons.attach_money), findsNWidgets(3));
    expect(find.text('restaurant, '), findsOneWidget);
    expect(find.text('hotel'), findsOneWidget);
  });

}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
