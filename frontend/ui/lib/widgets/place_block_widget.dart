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

  @override
  Widget build(BuildContext context) {
    return ExpansionTile(
        title: Text(placeName),
        trailing: RaisedButton(
          onPressed: () => {},
          child: Text('M T D'),
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

/*decoration: BoxDecoration(
          border: Border.all(
            color: Colors.black,
          ),
          borderRadius: BorderRadius.all(Radius.circular(5))),
      margin: const EdgeInsets.all(15.0),
      padding: const EdgeInsets.all(10.0),
      */
