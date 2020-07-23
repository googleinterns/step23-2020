import 'package:flutter/material.dart';

// A Widget for making a linkable button
class RoutingButton extends StatelessWidget {
  final String route;
  final Icon iconType;

  RoutingButton(this.route, this.iconType);

  @override
  Widget build(BuildContext context) {
    return RaisedButton(
      onPressed: () {
        Navigator.pushNamed(context, route);
      },
      child: iconType,
    );
  }
}
