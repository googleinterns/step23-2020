import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/router/router.dart';
import 'package:tripmeout/widgets/autocomplete_text_field_widget.dart';

//TODO: Add loading screen after to the Create Trip Widget
//TODO: Get rid of the newInformation text on the page.

class CreateTripWidget extends StatefulWidget {
  final TripService tripService;

  CreateTripWidget(this.tripService);

  @override
  _CreateTripWidgetState createState() => _CreateTripWidgetState();
}

class _CreateTripWidgetState extends State<CreateTripWidget> {
  TripService get tripService => widget.tripService;

  bool _enabled = false;

  String place;
  int radius = 0;
  String newInformation = 'Grabbed info placed here.';
  void submitTrip() {
    setState(() {
      tripService.createTrip(Trip(
        name: place,
        id: radius.toString(),
        // Everywhere Seattle now.
        placesApiPlaceId: 'ChIJVTPokywQkFQRmtVEaUZlJRA',
      ));
      newInformation = "$place ${radius}km";
    });
  }

  @override
  Widget build(BuildContext context) {
    var _onPressed;
    if (_enabled) {
      _onPressed = () {
        submitTrip();
      };
    }

    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.all(25.0),
          child: Container(
            width: 250.0,
            child: TextField(
              onChanged: (text) {
                if (text == '') {
                  place = text;
                  setState(() {
                    _enabled = false;
                  });
                } else {
                  place = text;
                  setState(() {
                    _enabled = true;
                  });
                }
              },
              textAlign: TextAlign.center,
              decoration: InputDecoration(
                border: OutlineInputBorder(),
                labelText: 'enter your trip name',
              ),
            ),
          ),
        ),
        MapsApiPlacesTextFieldWidget(['(cities)']),
        Padding(
          padding: const EdgeInsets.all(25.0),
          child: RaisedButton(
            onPressed: _onPressed,
            child: Text('Submit'),
          ),
        ),
      ],
    );
  }
}
