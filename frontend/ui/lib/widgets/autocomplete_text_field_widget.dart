import 'dart:async';
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
  final List<String> allowedTypes;

  _MapsApiPlacesTextFieldState(this.allowedTypes);

  String newInformation = "";

  void changeText(String place) {
    setState(() {
      newInformation = "$place";
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
              style: DefaultTextStyle.of(context).style.copyWith(
                fontStyle: FontStyle.italic
              ),
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
              changeText(suggestion.description);
            },
          ),
        ),
        Container(child: Text('$newInformation'))
      ],
    );
  }


  Future<List<AutocompletePrediction>> getAutocomplete(String input, List<String> allowedTypes) {
    if (input == null || input == "") {
      return Future.sync(() => []);
    }

    Completer<List<AutocompletePrediction>> completer = Completer();
    final AutocompletionRequest request = AutocompletionRequest()
      ..input = input
      ..types = allowedTypes;

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