import 'package:flutter/material.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/widgets/autocomplete_text_field_widget.dart';
import 'package:tripmeout/router/router.dart';
import 'package:tripmeout/model/place.dart';
import 'package:flutter/src/widgets/basic.dart' as basic;

//TODO: Add loading screen after to the Create Trip Widget

class CreateTripWidget extends StatefulWidget {
  final TripService tripService;
  final PlacesApiServices placesApiServices;

  CreateTripWidget(this.tripService, this.placesApiServices, {Key key}) : super(key: key);

  @override
  _CreateTripWidgetState createState() => _CreateTripWidgetState();
}

class _CreateTripWidgetState extends State<CreateTripWidget> {
  TripService get tripService => widget.tripService;
  PlacesApiServices get placesApiServices => widget.placesApiServices;

  bool _enabled = false;
  bool _showPics = false;
  Widget _imageWidget = Container();

  String userSpecifiedTripName;
  String placeId;

  Future<Trip> submitTrip() async {
    return tripService.createTrip(Trip(
      name: userSpecifiedTripName,
      placesApiPlaceId: placeId,
    ));
  }

  void displayImages(PlaceWrapper suggestion) async {
    List<String> imageUrls =
        await placesApiServices.getPhotos(suggestion.placeId);
    setState(() {
      _showPics = true;
      placeId = suggestion.placeId;
      if (imageUrls.length == 0) {
        _imageWidget = Text("No images found.");
      } else {
        List<Widget> imageWidgets = (imageUrls.map<Widget>((url) {
          return Card(child: Image(image: NetworkImage(url)));
        })).toList();

        _imageWidget = Container(
            height: 400,
            width: 500,
            child: ListView(
              children: imageWidgets,
              scrollDirection: Axis.horizontal,
            ));
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    var _onPressed;
    if (_enabled && _showPics) {
      _onPressed = () {
        submitTrip().then((trip) {
          Navigator.pushNamed(
              context, Router.createTripViewRoute(trip.id) + "/recommended");
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

    return ListView(
      children: [
        basic.Padding(
          padding: const EdgeInsets.all(25.0),
          child: Container(
            width: 250.0,
            child: TextField(
              onChanged: (text) {
                if (text == '') {
                  userSpecifiedTripName = text;
                  setState(() {
                    _enabled = false;
                  });
                } else {
                  userSpecifiedTripName = text;
                  setState(() {
                    _enabled = true;
                  });
                }
              },
              decoration: InputDecoration(
                border: OutlineInputBorder(),
                labelText: 'Enter your trip name',
              ),
            ),
          ),
        ),
        MapsApiPlacesTextFieldWidget(
            ['(cities)'], placesApiServices, displayImages),
        _imageWidget,
        basic.Padding(
          padding: const EdgeInsets.all(25.0),
          child: IconButton(
            onPressed: _onPressed,
            icon: Icon(Icons.send),
            tooltip: "Click this to submit your trip.",
            color: Colors.green,
          ),
        ),
      ],
    );
  }
}
