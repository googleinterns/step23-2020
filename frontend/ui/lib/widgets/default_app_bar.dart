import 'package:flutter/material.dart';
import 'package:tripmeout/router/router.dart';
import 'package:tripmeout/widgets/routing_button_widget.dart';

AppBar defaultAppBar(BuildContext context) {
  return AppBar(
    // TODO: Customize me yo.
    title: Row(children: [Icon(Icons.airplanemode_active), Text('TripMeOut')]),
    // Don't show app bar back button, it looks gross.
    actions: [
      RoutingButton(
          Router.createTripRoute, Icon(Icons.add), "Click to make New Trip"),
      RoutingButton(Router.tripListRoute, Icon(Icons.assignment),
          "Click to view your trips,")
    ],
  );
}
