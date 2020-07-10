import 'package:flutter/material.dart';

class TripViewWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Center(
        child: FittedBox(
      child: Row(
        children: <Widget>[
          Padding(
            padding: const EdgeInsets.all(5.0),
            child: Text("This is where the trip will be   "),
          ),
          Padding(
            padding: const EdgeInsets.all(3.0),
            child: Text("M"),
          ),
          Padding(
            padding: const EdgeInsets.all(3.0),
            child: Text("T"),
          ),
          Padding(
            padding: const EdgeInsets.all(3.0),
            child: Text("D"),
          ),
        ],
      ),
      fit: BoxFit.contain,
    ));
  }
}
