import 'package:flutter/material.dart';

/// A Widget for making a linkable button
class LinkButton extends StatelessWidget {
  final String link;
  final Icon iconType;

  LinkButton(this.link, this.iconType);

  @override
  Widget build(BuildContext context) {
    return RaisedButton(
      onPressed: () {
        Navigator.pushNamed(context, link);
      },
      child: iconType,
    );
  }
}
