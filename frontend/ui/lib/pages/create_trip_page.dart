import 'package:tripmeout/widgets/create_trip_widget.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:flutter/material.dart';

class CreateTripPage extends StatelessWidget {
  final TripService tripService;

  CreateTripPage(this.tripService, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: defaultAppBar(context),
      body: Center(child: CreateTripWidget(this.tripService)),
    );
  }
}
