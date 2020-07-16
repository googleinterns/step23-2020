import 'package:flutter/material.dart';
import 'package:tripmeout/widgets/trip_view_widget.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';

class TripViewPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: defaultAppBar(context),
      body: Center(child: TripViewWidget()),
    );
  }
}
