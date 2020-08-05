import 'package:flutter/material.dart';
import 'package:tripmeout/router/router.dart';
import 'package:tripmeout/widgets/routing_button_widget.dart';

AppBar defaultAppBar(BuildContext context) {
  return AppBar(
    title: Image.asset(
      'app_bar_logo_200x200.png',
      fit: BoxFit.contain,
    ),
    actions: [
      RoutingButton(
          Router.logInRoute, Icon(Icons.account_box), "Click to Login"),
      RoutingButton(
          Router.createTripRoute, Icon(Icons.add), "Click to make New Trip"),
      RoutingButton(Router.tripListRoute, Icon(Icons.assignment),
          "Click to view your trips")
    ],
  );
}

AppBar defaultAppBarWithNoExtras(BuildContext context) {
  return AppBar(
    title: Image.asset(
      'app_bar_logo_200x200.png',
      fit: BoxFit.contain,
    ),
    automaticallyImplyLeading: false, //Gets rid of appBar back arrow
    actions: [
      RoutingButton(
          Router.logInRoute, Icon(Icons.account_box), "Click to Login"),
      RoutingButton(Router.tripListRoute, Icon(Icons.assignment),
          "Click to view your trips")
    ],
  );
}
