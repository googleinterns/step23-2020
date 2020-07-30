import 'package:flutter/material.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/services/place_visit_service.dart';

class PlaceBlockWidget extends StatefulWidget {
  final PlaceVisit placeVisit;
  final PlaceVisitService placeVisitService;

  PlaceBlockWidget(this.placeVisit, this.placeVisitService);

  @override
  State createState() => _PlaceBlockWidgetState(placeVisit, placeVisitService);
}

class _PlaceBlockWidgetState extends State<PlaceBlockWidget> {
  final PlaceVisit placeVisit;
  final PlaceVisitService placeVisitService;
  _PlaceBlockWidgetState(this.placeVisit, this.placeVisitService);

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

    if (placeVisit.userMark == UserMark.YES) {
      setState(() {
        _selected = true;
        _icon = Icon(Icons.favorite);
        _color = Colors.red;
      });
    }

    return ExpansionTile(
        initiallyExpanded: true,
        title: Text(placeVisit.name),
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
                      placeVisit..userMark = UserMark.MAYBE;
                    } else {
                      _selected = true;
                      _icon = Icon(Icons.favorite);
                      _color = Colors.pink;
                      placeVisit..userMark = UserMark.YES;
                    }
                  });
                  placeVisitService.updatePlaceVisitUserMark(placeVisit);
                },
                color: _color,
                tooltip: "Must Go",
              ),
              IconButton(
                onPressed: () => placeVisitService.deletePlaceVisit(placeVisit.tripid, placeVisit.id),
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
