import 'package:tripmeout/widgets/create_trip_widget.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';
import 'package:flutter/material.dart';

class CreateTripPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: defaultAppBar(context),
      body: Center(child: CreateTripWidget()),
    );
  }
}
