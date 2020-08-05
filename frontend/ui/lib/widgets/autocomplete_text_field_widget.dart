import 'package:flutter/material.dart';
import 'package:flutter_typeahead/flutter_typeahead.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/model/place.dart';

class MapsApiPlacesTextFieldWidget<T> extends StatefulWidget {
  final List<String> allowedTypes;
  final PlacesApiServices placesApiServices;
  final _OnClick<T> onClick;

  MapsApiPlacesTextFieldWidget(
      this.allowedTypes, this.placesApiServices, this.onClick);

  @override
  _MapsApiPlacesTextFieldState createState() => _MapsApiPlacesTextFieldState();
}

typedef _OnClick<T> = void Function(PlaceWrapper);

class _MapsApiPlacesTextFieldState<T>
    extends State<MapsApiPlacesTextFieldWidget<T>> {
  final TextEditingController _typeAheadController = TextEditingController();
  PlacesApiServices get placesApiServices => widget.placesApiServices;
  List<String> get allowedTypes => widget.allowedTypes;
  _OnClick<T> get onClick => widget.onClick;

  _MapsApiPlacesTextFieldState();

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.all(25.0),
          child: TypeAheadField<PlaceWrapper>(
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
              onClick.call(suggestion);
            },
            noItemsFoundBuilder: (BuildContext context) =>
                Text('Please enter a city'),
          ),
        ),
      ],
    );
  }
}
