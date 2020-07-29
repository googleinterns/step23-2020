import 'package:flutter/material.dart';

// A Widget for making a linkable button
class RoutingButton extends StatelessWidget {
  final String description;
  final String route;
  final Icon iconType;

  RoutingButton(this.route, this.iconType, this.description);

  @override
  Widget build(BuildContext context) {
    return IconButton(
          onPressed: () {
            Navigator.pushNamed(context, route);
          },
          icon: iconType,
          tooltip: description,
        );
  }
}

class FloatingRoutingButton extends StatelessWidget {
  final String description;
  final String route;
  final Icon iconType;

  FloatingRoutingButton(this.route, this.iconType, this.description);

  @override
  Widget build(BuildContext context) {
    return FloatingActionButton(
          onPressed: () {
            Navigator.pushNamed(context, route);
          },
          child: iconType,
          tooltip: description,
        );
  }
}
