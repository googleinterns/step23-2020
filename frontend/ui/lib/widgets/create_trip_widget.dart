import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:tripmeout/widgets/map_widget.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/model/location.dart';
import 'package:tripmeout/router/router.dart';

//TODO: Add loading screen after to the Create Trip Widget
//TODO: Get rid of the newInformation text on the page.

class CreateTripWidget extends StatefulWidget {
  final TripService tripService;

  CreateTripWidget(this.tripService);

  @override
  _CreateTripWidgetState createState() =>
      _CreateTripWidgetState(this.tripService);
}

class _CreateTripWidgetState extends State<CreateTripWidget> {
  final TripService tripService;

  _CreateTripWidgetState(this.tripService);

  String place = "No Input";
  int radius = 0;
  String newInformation = 'Grabbed info placed here.';
  void submitTrip() {
    setState(() {
      tripService.createTrip(Trip(
          name: "$place",
          id: radius.toString(),
          location: Location(latitude: 10.0, longitude: 10.0)));

      newInformation = "$place ${radius}km";
    });
  }

  @override
  Widget build(BuildContext context) {
    return FittedBox(
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(25.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Container(
                  width: 250.0,
                  child: TextField(
                    onChanged: (text) {
                      place = text;
                    },
                    textAlign: TextAlign.center,
                    decoration: InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Enter your Location',
                    ),
                  ),
                ),
                Container(
                  width: 125.0,
                  child: TextField(
                    onChanged: (text) {
                      radius = int.parse(text);
                    },
                    textAlign: TextAlign.center,
                    keyboardType: TextInputType.number,
                    inputFormatters: <TextInputFormatter>[
                      WhitelistingTextInputFormatter.digitsOnly
                    ],
                    decoration: InputDecoration(
                        border: OutlineInputBorder(), labelText: 'Radius KM'),
                  ),
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(25.0),
            child: MapWidget(),
          ),
          Container(child: Text('$newInformation')),
          Padding(
            padding: const EdgeInsets.all(25.0),
            child: RaisedButton(
              onPressed: () => {
                submitTrip(),
              },
              child: Text('Submit'),
            ),
          ),
        ],
      ),
      fit: BoxFit.contain,
    );
  }
}
