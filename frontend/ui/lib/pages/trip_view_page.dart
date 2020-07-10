import 'package:flutter/material.dart';
import 'package:tripmeout/widgets/trip_view_widget.dart';

class TripViewPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Trips'),
      ),
      body: TripViewWidget(),
    );
  }
}
