import 'package:flutter/material.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';
import 'package:tripmeout/widgets/trip_list_widget.dart';

class TripListPage extends StatelessWidget {
  final TripService tripService;

  TripListPage(this.tripService);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: defaultAppBar(context),
      body: TripListWidget(this.tripService),
    );
  }
}
