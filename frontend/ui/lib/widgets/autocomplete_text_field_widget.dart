import 'dart:async';
import 'dart:html';
import 'package:flutter/material.dart';
import 'package:google_maps/google_maps.dart';
import 'package:google_maps/google_maps_places.dart';
import 'package:flutter_typeahead/flutter_typeahead.dart';
import 'package:flutter/src/widgets/basic.dart' as basic;

class MapsApiPlacesTextFieldWidget extends StatefulWidget {
  final List<String> allowedTypes;
  MapsApiPlacesTextFieldWidget(this.allowedTypes);

  @override
  _MapsApiPlacesTextFieldState createState() => _MapsApiPlacesTextFieldState(allowedTypes);
}

class _MapsApiPlacesTextFieldState extends State<MapsApiPlacesTextFieldWidget> {
  final TextEditingController _typeAheadController = TextEditingController();
  final AutocompleteService autocompleteService = AutocompleteService();
  final PlacesService placesService = PlacesService(document.getElementById("maps"));
  final List<String> allowedTypes;

  _MapsApiPlacesTextFieldState(this.allowedTypes);

  bool show = false;
  String placeId = "";

  void showImage(String placeId) {
      setState(() {
          this.placeId = placeId;
          this.show = true;
      });
  }


  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        basic.Padding(
          padding: const EdgeInsets.all(25.0),
          child: TypeAheadField<AutocompletePrediction>(
            textFieldConfiguration: TextFieldConfiguration(
              autofocus: true,
              decoration: InputDecoration(
                border: OutlineInputBorder(),
                labelText: 'Enter your Location',
              ),
              controller: this._typeAheadController
            ),
            suggestionsCallback: (pattern) => getAutocomplete(pattern, allowedTypes),
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
      ],
    );
  }


  Future<List<AutocompletePrediction>> getAutocomplete(String input, List<String> allowedTypes) {
    if (input == null || input == "") {
      return Future.sync(() => []);
    }

    Completer<List<AutocompletePrediction>> completer = Completer();
    AutocompletionRequest request = AutocompletionRequest()
      ..input = input;

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

  // TO-DO figure out what's wrong with this request
  Container getPhotos(String placeId) {
    List<String> images = [];
    final request = PlaceDetailsRequest()
      ..placeId = placeId;

    placesService.getDetails(request, (result, status) {
      if (status == PlacesServiceStatus.OK) {
        changeText("OK");
        final photoOptions = PhotoOptions()
          ..maxHeight = 50
          ..maxWidth = 50;
        images = result.photos.map((photo) => photo.getUrl(photoOptions));
      } 
    });

    if (images.length == 0) {
        return Container(child: Text(newInformation));
    } else {
        return Container(child: Row(children: images.map<Widget>((url) => Image(image: NetworkImage(url)))));
    }
  }

}