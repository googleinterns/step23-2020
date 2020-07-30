import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/widgets/autocomplete_text_field_widget.dart';
import 'package:tripmeout/router/router.dart';
import 'package:google_maps/google_maps_places.dart';

//TODO: Add loading screen after to the Create Trip Widget

class CreatePlaceVisitWidget extends StatefulWidget {
  final PlaceVisitService placeVisitService;
  final PlacesApiServices placesApiServices;

  CreatePlaceVisitWidget(this.placeVisitService, this.placesApiServices);

  @override
  _CreatePlaceVisitWidgetState createState() => _CreatePlaceVisitWidgetState();
}

class _CreatePlaceVisitWidgetState extends State<CreatePlaceVisitWidget> {
  PlaceVisitService get placeVisitService => widget.placeVisitService;
  PlacesApiServices get placesApiServices => widget.placesApiServices;

  bool _enabled = false;

  String placesApiSpecifiedName;
  String placeId;
  String tripId;
  UserMark userMark = UserMark.UNKNOWN;

  Future<PlaceVisit> submitPlaceVisit() async {
    return placeVisitService.createPlaceVisit(PlaceVisit(
      name: placesApiSpecifiedName,
      tripid: tripId,
      placesApiPlaceId: placeId,
      userMark: userMark,
    ));
  }

  void setFields(PlaceVisit suggestion) {
    setState(() {
      placeId = suggestion.placesApiPlaceId;
      placesApiSpecifiedName = suggestion.name;
    });
  }

  @override
  Widget build(BuildContext context) {
    var _onPressed;
    if (_enabled) {
      _onPressed = () {
        submitPlaceVisit().then((placeVisit) {
          Navigator.pushNamed(
              context, "/trip/${placeVisit.tripid}/placeVisits");
        }, onError: (error) {
          Scaffold.of(context).showSnackBar(SnackBar(
            content: Text("Error creating trip"),
            action: SnackBarAction(
              label: "Dismiss",
              onPressed: () {},
            ),
          ));
        });
      };
    }

    List<bool> _selections = List.generate(2, (_) => false);

    return Column(
      children: [
        Row(children: [
          MapsApiPlacesTextFieldWidget([], placesApiServices, setFields),
          ToggleButtons(
            children: [
              Tooltip(message: "MUST GO", child: Icon(Icons.favorite)),
              Tooltip(message: "TIME PERMITS", child: Icon(Icons.alarm)),
            ],
            onPressed: (int index) {
              setState(() {
                if (index == 0) {
                  userMark = UserMark.YES;
                } else {
                  userMark = UserMark.MAYBE;
                }

                for (int buttonIndex = 0;
                    buttonIndex < _selections.length;
                    buttonIndex++) {
                  if (buttonIndex == index) {
                    _selections[buttonIndex] = true;
                  } else {
                    _selections[buttonIndex] = false;
                  }
                }
              });
            },
            isSelected: _selections,
            color: Colors.black,
            selectedColor: Theme.of(context).accentColor,
          ),
        ]),
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
