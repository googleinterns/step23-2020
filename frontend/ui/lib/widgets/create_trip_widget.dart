import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/model/location.dart';
import 'package:tripmeout/router/router.dart';
import 'package:google_maps/google_maps.dart';
import 'package:google_maps/google_maps_places.dart';
import 'package:flutter_typeahead/flutter_typeahead.dart';
import 'package:flutter/src/widgets/basic.dart' as basic;

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
  final TextEditingController _typeAheadController = TextEditingController();
  final AutocompleteService autocompleteService = AutocompleteService();

  bool _enabled = false;
  bool show = false;
  String placeId = "";

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

    void showImage(String placeId) {
      setState(() {
        this.placeId = placeId;
        this.show = true;
      });
    }

    return Column(
      children: [
        basic.Padding(
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
        basic.Padding(
          padding: const EdgeInsets.all(25.0),
          child: TypeAheadField<AutocompletePrediction>(
            textFieldConfiguration: TextFieldConfiguration(
                autofocus: true,
                decoration: InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: 'Enter your Location',
                ),
                controller: this._typeAheadController),
            suggestionsCallback: (pattern) =>
                getAutocomplete(pattern, ['(cities)']),
            itemBuilder: (context, suggestion) {
              return ListTile(
                title: Text(suggestion.description),
              );
            },
            onSuggestionSelected: (suggestion) {
              this._typeAheadController.text = suggestion.description;
              showImage(suggestion.placeId);
            },
          ),
        ),
        //show ? getPhotos(this.placeId) : Container(),
        basic.Padding(
          padding: const EdgeInsets.all(25.0),
          child: RaisedButton(
            onPressed: _onPressed,
            child: Text('Submit'),
          ),
        ),
      ],
    );
  }

  Future<List<AutocompletePrediction>> getAutocomplete(
      String input, List<String> allowedTypes) {
    if (input == null || input == "") {
      return Future.sync(() => []);
    }

    Completer<List<AutocompletePrediction>> completer = Completer();
    AutocompletionRequest request = AutocompletionRequest()..input = input;

    if (allowedTypes.length > 0) {
      request = request..types = allowedTypes;
    }

    autocompleteService.getPlacePredictions(request, (result, status) {
      if (status == PlacesServiceStatus.OK) {
        completer.complete(result);
      } else if (status == PlacesServiceStatus.ZERO_RESULTS) {
        completer.complete([]);
      } else {
        completer.completeError(status);
      }
    });

    return completer.future;
  }
}
