import 'package:flutter/material.dart';

class PlaceBlockWidget extends StatefulWidget {
  final String placeName;

  PlaceBlockWidget(this.placeName);

  @override
  State createState() => _PlaceBlockWidgetState(placeName);
}

class _PlaceBlockWidgetState extends State<PlaceBlockWidget> {
  final String placeName;
  _PlaceBlockWidgetState(this.placeName);

  List<bool> _selections = List.generate(3, (_) => false);

  @override
  Widget build(BuildContext context) {
    return ExpansionTile(
        title: Text(placeName),
        trailing: ToggleButtons(
          children: <Widget>[
            Text('M'),
            Text('T'),
            Text('D')
          ],
          onPressed: (int index) {
            setState(() {
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
        children: [
          Column(children: [
            Text('Description'),
            Text('Foo'),
            Text('Bar'),
            Text('Baz'),
          ])
        ]);
  }
}
