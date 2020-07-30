import 'package:flutter/material.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/trip_view_widget.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';

class TripViewPage extends StatelessWidget {
  final TripService tripService;
  final String tripId;

  TripViewPage(this.tripService, this.tripId, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: defaultAppBar(context),
      body: TripViewWidgetFromService(this.tripService, this.tripId),
    );
  }
}
