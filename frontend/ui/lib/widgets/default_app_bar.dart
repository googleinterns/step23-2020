import 'package:flutter/material.dart';
import 'package:tripmeout/router/router.dart';
import 'package:tripmeout/widgets/routing_button_widget.dart';

AppBar defaultAppBar(BuildContext context) {
  return AppBar(
    title: Row(children: [Icon(Icons.airplanemode_active), Text('TripMeOut')]),
    actions: [
      RoutingButton(
          Router.createTripRoute, Icon(Icons.add), "Click to make New Trip"),
      RoutingButton(Router.tripListRoute, Icon(Icons.assignment),
          "Click to view your trips,")
    ],
  );
}

AppBar defaultAppBarWithNoBackArrow(BuildContext context) {
  return AppBar(
    title: Row(children: [Icon(Icons.airplanemode_active), Text('TripMeOut')]),
    automaticallyImplyLeading: false, //Gets rid of appBar back arrow
    actions: [
      RoutingButton(
          Router.createTripRoute, Icon(Icons.add), "Click to make New Trip"),
      RoutingButton(Router.tripListRoute, Icon(Icons.assignment),
          "Click to view your trips,")
    ],
  );
}
