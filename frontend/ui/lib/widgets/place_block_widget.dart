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

  List<bool> _selections = List.generate(2, (_) => false);

  @override
  Widget build(BuildContext context) {
    return ExpansionTile(
        title: Text(placeName),
        trailing: Container(
            width: 250.0,
            child: Row(children: [
              ToggleButtons(
                children: [
                  Tooltip(message: "This selection makes the place a MUST GO!", child: Text('M')),
                  Tooltip(message: "This selection makes the place optional for if TIME PERMITS.", child: Text('T')),
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
              IconButton(
                onPressed: () => {},
                icon: Icon(Icons.delete),
                color: Colors.red,
              ),
            ])),
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
