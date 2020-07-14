import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';

void main() {
  testWidgets('displays expected title', (WidgetTester tester) async {
    await tester.pumpWidget(_InTestableContext(defaultAppBar));

    expect(find.text('TripMeOut'), findsOneWidget);
  });
}

typedef _AppBarProvider = Widget Function(BuildContext);

class _InTestableContext extends StatelessWidget {
  final _AppBarProvider appBarProvider;

  _InTestableContext(this.appBarProvider);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: appBarProvider.call(context),
        body: Container(),
      ),
    );
  }
}
