import 'package:flutter/material.dart';
import 'package:tripmeout/model/place.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/widgets/autocomplete_text_field_widget.dart';
import 'package:tripmeout/router/router.dart';

//TODO: Add loading screen after to the Create Trip Widget

class CreatePlaceVisitWidget extends StatefulWidget {
  final PlaceVisitService placeVisitService;
  final PlacesApiServices placesApiServices;
  final String tripId;

  CreatePlaceVisitWidget(
      this.placeVisitService, this.placesApiServices, this.tripId);

  @override
  _CreatePlaceVisitWidgetState createState() => _CreatePlaceVisitWidgetState();
}

class _CreatePlaceVisitWidgetState extends State<CreatePlaceVisitWidget> {
  PlaceVisitService get placeVisitService => widget.placeVisitService;
  PlacesApiServices get placesApiServices => widget.placesApiServices;
  String get tripId => widget.tripId;

  bool _enabled = false;
  bool _selected = false;
  Icon _icon = Icon(Icons.favorite_border);
  Color _color = Colors.black;
  Widget _imageWidget = Container();

  String placesApiSpecifiedName;
  String placeId;
  UserMark userMark = UserMark.MAYBE;

  Future<PlaceVisit> submitPlaceVisit() async {
    return placeVisitService.createPlaceVisit(PlaceVisit(
      name: placesApiSpecifiedName,
      tripid: tripId,
      placesApiPlaceId: placeId,
      userMark: userMark,
    ));
  }

  void setFields(PlaceWrapper suggestion) async {
    List<String> imageUrls =
        await placesApiServices.getPhotos(suggestion.placeId);
    print(suggestion.placeId);
    setState(() {
      _enabled = true;
      placeId = suggestion.placeId;
      placesApiSpecifiedName = suggestion.name;
      if (imageUrls.length == 0) {
        _imageWidget = Text("No images found.");
      } else {
        List<Widget> imageWidgets = (imageUrls.map<Widget>((url) {
          return Card(child: Image(image: NetworkImage(url)));
        })).toList();

        _imageWidget = Container(
          height: 400,
          width: 1000,
          child: ListView(
            children: imageWidgets,
            scrollDirection: Axis.horizontal,
          ),
        );
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    var _onPressed;
    if (_enabled) {
      _onPressed = () {
        submitPlaceVisit().then((placeVisit) {
          Navigator.pushNamed(
              context, Router.createTripViewRoute(placeVisit.tripid));
        }, onError: (error) {
          Scaffold.of(context).showSnackBar(SnackBar(
            content: Text("Error creating place visit"),
            action: SnackBarAction(
              label: "Dismiss",
              onPressed: () {},
            ),
          ));
        });
      };
    }

    return Column(
      children: [
        Row(mainAxisAlignment: MainAxisAlignment.center, children: [
          Container(
              width: 600,
              child: MapsApiPlacesTextFieldWidget(
                  [], placesApiServices, setFields)),
          Container(
            width: 100.0,
            child: IconButton(
              icon: _icon,
              onPressed: () {
                setState(() {
                  if (_selected == true) {
                    _selected = false;
                    _icon = Icon(Icons.favorite_border);
                    _color = Colors.black;
                    userMark = UserMark.MAYBE;
                  } else {
                    _selected = true;
                    _icon = Icon(Icons.favorite);
                    _color = Colors.pink;
                    userMark = UserMark.YES;
                  }
                });
              },
              color: _color,
              tooltip: "Must Go",
            ),
          ),
        ]),
        _imageWidget,
        Padding(
          padding: const EdgeInsets.all(25.0),
          child: IconButton(
            onPressed: _onPressed,
            icon: Icon(Icons.send),
            tooltip: "Click this to submit your place to visit.",
            color: Colors.green,
          ),
        ),
      ],
    );
  }
}
