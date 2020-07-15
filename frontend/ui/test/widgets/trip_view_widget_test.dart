import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:tripmeout/widgets/trip_view_widget.dart';

void main() {
  testWidgets('Showing the text on page correctly shows up',
      (WidgetTester tester) async {
    var tripViewWidget = TripViewWidget();
    await tester.pumpWidget(wrapForDirectionality(tripViewWidget));
    await tester.pumpAndSettle();

    // Should be everything on the view trip screen without being expanded...
    expect(find.text('Trip Name'), findsOneWidget);
    expect(find.text('M T D'), findsOneWidget);
    expect(find.text("Place 1"), findsNothing);
    expect(find.text("Place 2"), findsNothing);
    expect(find.text("Place 3"), findsNothing);
    expect(find.text("Place 4"), findsNothing);
    expect(find.text("Place 5"), findsNothing);
  });

  testWidgets('Showing the expanded page correctly shows up',
      (WidgetTester tester) async {
    var tripViewWidget = TripViewWidget();
    await tester.pumpWidget(wrapForDirectionality(tripViewWidget));
    await tester.pumpAndSettle();

    expect(find.text('Trip Name'), findsOneWidget);
    expect(find.text('M T D'), findsOneWidget);
    expect(find.text("Place 1"), findsNothing);
    expect(find.text("Place 2"), findsNothing);
    expect(find.text("Place 3"), findsNothing);
    expect(find.text("Place 4"), findsNothing);
    expect(find.text("Place 5"), findsNothing);
    await tester.tap(find.byType(ExpansionTile));
    await tester.pumpAndSettle();

    // Should be everything on the view trip screen with expansion...
    expect(find.text('Trip Name'), findsOneWidget);
    expect(find.text('M T D'), findsOneWidget);
    expect(find.text("Place 1"), findsOneWidget);
    expect(find.text("Place 2"), findsOneWidget);
    expect(find.text("Place 3"), findsOneWidget);
    expect(find.text("Place 4"), findsOneWidget);
    expect(find.text("Place 5"), findsOneWidget);
  });
}

Widget wrapForDirectionality(Widget wrapped) {
  return MaterialApp(home: Scaffold(body: wrapped));
}
