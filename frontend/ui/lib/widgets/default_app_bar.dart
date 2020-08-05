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
      LoginRouteButton(),
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
      LoginRouteButton(),
      RoutingButton(Router.tripListRoute, Icon(Icons.assignment),
          "Click to view your trips")
    ],
  );
}

class LoginRouteButton extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return IconButton(
      onPressed: () => Router.routeToLoginPage(context),
      icon: Icon(Icons.account_box),
      tooltip: 'Click to Login',
    );
  }
}
