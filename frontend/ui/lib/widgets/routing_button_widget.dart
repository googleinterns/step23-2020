import 'package:flutter/material.dart';

// A Widget for making a linkable button
class RoutingButton extends StatelessWidget {
  final String description;
  final String route;
  final Icon iconType;

  RoutingButton(this.route, this.iconType, this.description);

  @override
  Widget build(BuildContext context) {
    return Tooltip(
        message: description,
        child: IconButton(
          onPressed: () {
            Navigator.pushNamed(context, route);
          },
          icon: iconType,
        ));
  }
}

class FloatingRoutingButton extends StatelessWidget {
  final String description;
  final String route;
  final Icon iconType;

  FloatingRoutingButton(this.route, this.iconType, this.description);

  @override
  Widget build(BuildContext context) {
    return Tooltip(
        message: description,
        child: FloatingActionButton(
          onPressed: () {
            Navigator.pushNamed(context, route);
          },
          child: iconType,
        ));
  }
}
