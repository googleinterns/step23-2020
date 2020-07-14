import 'package:flutter/material.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/trip_list_widget.dart';

class TripListPage extends StatelessWidget {
  final TripService tripService;

  TripListPage(this.tripService);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Trips'),
      ),
      body: TripListWidget(this.tripService),
    );
  }
}
