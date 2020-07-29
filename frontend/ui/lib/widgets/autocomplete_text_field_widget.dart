import 'dart:async';
import 'package:flutter/material.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:flutter_typeahead/flutter_typeahead.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:flutter/src/widgets/basic.dart' as basic;

class MapsApiPlacesTextFieldWidget extends StatefulWidget {
  final List<String> allowedTypes;
  final PlacesApiServices placesApiServices;
  MapsApiPlacesTextFieldWidget(this.allowedTypes, this.placesApiServices);

  @override
  _MapsApiPlacesTextFieldState createState() =>
      _MapsApiPlacesTextFieldState();
}

class _MapsApiPlacesTextFieldState extends State<MapsApiPlacesTextFieldWidget> {
  final TextEditingController _typeAheadController = TextEditingController();
  PlacesApiServices get placesApiServices => widget.placesApiServices;
  List<String> get allowedTypes => widget.allowedTypes;

  _MapsApiPlacesTextFieldState();

  String placeId = "";
  Future<List<String>> image;
  bool show = false;

  void showImage(String placeId, bool show) {
    setState(() {
      this.placeId = placeId;
      this.show = show;
      if (show) {
        this.image = placesApiServices.getPhotos(this.placeId);
      } else {
          this.image = null;
      }

    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        basic.Padding(
          padding: const EdgeInsets.all(25.0),
          child: TypeAheadField<PlaceVisit>(
            textFieldConfiguration: TextFieldConfiguration(
                autofocus: true,
                decoration: InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: 'Enter your Destination',
                ),
                controller: this._typeAheadController),
            suggestionsCallback: (pattern) =>
                placesApiServices.getAutocomplete(pattern, allowedTypes),
            itemBuilder: (context, suggestion) {
              return ListTile(
                title: Text(suggestion.name),
              );
            },
            onSuggestionSelected: (suggestion) {
              this._typeAheadController.text = suggestion.name;
              showImage(suggestion.placesApiPlaceId, true);
            },
            noItemsFoundBuilder: (BuildContext context) => Text('Please enter a city'),
          ),
        ),
        FutureBuilder<List<String>>(
            future: image,
            builder: (BuildContext context, AsyncSnapshot<List<String>> snapshot) {
              if (snapshot.hasData) {
                List<Widget> imageWidgets = imagesToContainer(snapshot.data);
                return Container(height: 450.0, width: 500, child: ListView(children: imageWidgets));
              } else {
                if (show) {
                    return CircularProgressIndicator();
                } else {
                    return Container();
                }
                
              }
            },
        )
      ],
    );
  }

  List<Widget> imagesToContainer(List<String> images) {
    if (images.length == 0) {
      return [Text("No images found.")];
    } else {
      List<Widget> imageWidgets = (images.map<Widget>((url) {
        return Card(child: Image(image: NetworkImage(url)));
      })).toList();
      return imageWidgets;
    }
  }
}
