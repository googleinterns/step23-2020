import 'package:tripmeout/widgets/create_trip_widget.dart';
import 'package:flutter/material.dart';

class CreateTripPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Trips'),
      ),
      body: Center(child: CreateTripWidget()),
    );
  }
}
