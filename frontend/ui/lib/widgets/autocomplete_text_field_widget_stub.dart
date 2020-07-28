import 'package:flutter/material.dart';
import 'package:tripmeout/services/places_services.dart';

// TODO: Decompose the auto-complete widget into the smallest possible component
// that requires dart:html. Doing so will simplify this stub code itself which
// only runs during local tests. Tests that check the stub aren't useful anyway
// because this stub is never displayed in the real UI.

class MapsApiPlacesTextFieldWidget extends StatefulWidget {
  MapsApiPlacesTextFieldWidget(List<String> allowedTypes, PlacesApiServices placesApiServices);

  @override
  _MapsApiPlacesTextFieldState createState() => _MapsApiPlacesTextFieldState();
}

class _MapsApiPlacesTextFieldState extends State<MapsApiPlacesTextFieldWidget> {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        TextField(
          decoration: InputDecoration(
            labelText: "Enter your Location",
          ),
        ),
        TextField(
          decoration: InputDecoration(
            labelText: "Radius KM",
          ),
        )
      ],
    );
  }
}
