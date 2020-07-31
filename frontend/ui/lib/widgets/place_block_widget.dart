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

  List<Widget> pictures = new List<Widget>();
  List<Color> colors = [
    Colors.red,
    Colors.blue,
    Colors.green,
    Colors.yellow,
    Colors.orange,
  ];

  bool _selected = false;
  Icon _icon = Icon(Icons.favorite_border);
  Color _color = Colors.black;

  @override
  Widget build(BuildContext context) {
    return ExpansionTile(
        initiallyExpanded: true,
        title: Text(placeName),
        trailing: Container(
            width: 100.0,
            child: Row(children: [
              IconButton(
                icon: _icon,
                onPressed: () {
                  setState(() {
                    if (_selected == true) {
                      _selected = false;
                      _icon = Icon(Icons.favorite_border);
                      _color = Colors.black;
                    } else {
                      _selected = true;
                      _icon = Icon(Icons.favorite);
                      _color = Colors.pink;
                    }
                  });
                },
                color: _color,
                tooltip: "Must Go",
              ),
              IconButton(
                onPressed: () => {}, //TODO: Add delete place method to Button
                icon: Icon(Icons.delete),
                color: Colors.red,
                tooltip: "Delete this place",
              ),
            ])),
        children: [
          Container(
              height: 200,
              child: ListView(children: [
                Container(
                    height: 100, child: Center(child: Text('Description'))),
                Container(height: 200, child: getPictureWidgets(colors)),
                Container(height: 100, child: Center(child: Text('Bar'))),
                Container(height: 100, child: Center(child: Text('Baz'))),
              ]))
        ]);
  }
}

Widget getPictureWidgets(List<Color> colors) {
  return new ListView(
      scrollDirection: Axis.horizontal,
      children: colors
          .map((item) => new Container(width: 200.0, color: item))
          .toList());
}
