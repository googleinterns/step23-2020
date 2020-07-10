import 'package:flutter/material.dart';
import 'package:tripmeout/widgets/create_trip_widget.dart';
import 'package:tripmeout/widgets/map_widget.dart';

class CreateTripPageWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Center(
        child: FittedBox(
      child: Column(
        children: <Widget>[
          Row(
            children: <Widget>[
              Padding(
                padding: const EdgeInsets.all(25.0),
                child: MapPlaceHolder(),
              ),
            ],
          ),
          Row(
            children: <Widget>[
              Container(
                child: Text(
                    "This should be the submit button under the fake map. I hope!!!"),
              ),
            ],
          ),
        ],
      ),
      fit: BoxFit.contain,
    ));
  }
}
