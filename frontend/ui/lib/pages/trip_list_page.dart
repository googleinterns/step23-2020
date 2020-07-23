import 'package:flutter/material.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';
import 'package:tripmeout/widgets/routing_button_widget.dart';
import 'package:tripmeout/widgets/trip_list_widget.dart';
import 'package:tripmeout/router/router.dart';

class TripListPage extends StatelessWidget {
  final TripService tripService;

  TripListPage(this.tripService);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: defaultAppBar(context),
        body: Column(children: [
          RoutingButton(Router.createTripRoute, Icon(Icons.add)),
          Expanded(
            child: ServiceLoadedTripListWidget(this.tripService),
          )
        ]));
  }
}
