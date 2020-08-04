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

class MockPlaceVisitService extends Mock implements PlaceVisitService {}

void main() {
  testWidgets('Showing the each place has correct widgets when not expanded',
      (WidgetTester tester) async {
    PlaceVisitService placeVisitService = MockPlaceVisitService();
    PlaceVisit placeVisit = PlaceVisit(id: 'id', name: 'Place1');

    var userStatusWidget = UserStatusWidget(placeVisitService, placeVisit);
    await tester.pumpWidget(wrapForDirectionality(userStatusWidget));
    await tester.pumpAndSettle();

    expect(find.byIcon(Icons.favorite), findsNothing);
    expect(find.byIcon(Icons.favorite_border), findsOneWidget);
    expect(find.byIcon(Icons.delete), findsOneWidget);

    //Shows that it toggles between favorite and not
  });

  testWidgets('showing that tapping on icons sends requests and changes icon',
      (WidgetTester tester) async {
    PlaceVisitService placeVisitService = MockPlaceVisitService();
    PlaceVisit placeVisit = PlaceVisit(id: 'id', name: 'Place1');

    when(placeVisitService.updatePlaceVisitUserMark(any))
        .thenAnswer((t) => Future.value(t.positionalArguments[0]));

    var userStatusWidget = UserStatusWidget(placeVisitService, placeVisit);
    await tester.pumpWidget(wrapForDirectionality(userStatusWidget));
    await tester.pumpAndSettle();

    await tester.tap(find.widgetWithIcon(IconButton, Icons.favorite_border));
    await tester.pumpAndSettle();

    expect(find.byIcon(Icons.favorite_border), findsNothing);
    expect(find.byIcon(Icons.favorite), findsOneWidget);

    PlaceVisit createdPlaceVisit =
        verify(placeVisitService.updatePlaceVisitUserMark(captureAny))
            .captured
            .single;

    expect(createdPlaceVisit.name, equals('Place1'));
    expect(createdPlaceVisit.userMark, equals(UserMark.YES));

    await tester.tap(find.widgetWithIcon(IconButton, Icons.favorite));
    await tester.pumpAndSettle(Duration(seconds: 2));

    expect(find.byIcon(Icons.favorite_border), findsOneWidget);
    expect(find.byIcon(Icons.favorite), findsNothing);

    createdPlaceVisit =
        verify(placeVisitService.updatePlaceVisitUserMark(captureAny))
            .captured
            .single;

    expect(createdPlaceVisit.name, equals('Place1'));
    expect(createdPlaceVisit.userMark, equals(UserMark.MAYBE));
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
