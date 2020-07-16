import 'package:flutter/material.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';

class TripViewWidget extends StatefulWidget {
  TripViewWidget({Key key}) : super(key: key);
  _TripWidgetState createState() => _TripWidgetState();
}

class _TripWidgetState extends State<TripViewWidget> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
          border: Border.all(
            color: Colors.black,
          ),
          borderRadius: BorderRadius.all(Radius.circular(5))),
      margin: const EdgeInsets.all(15.0),
      padding: const EdgeInsets.all(10.0),
      width: MediaQuery.of(context).size.width * .80,
      child: ExpansionTile(
        title: Text("Trip Name"),
        trailing: Text("M T D"),
        children: [
          Column(
            children: [
              PlaceBlockWidget("Place 1"),
              PlaceBlockWidget("Place 2"),
              PlaceBlockWidget("Place 3"),
              PlaceBlockWidget("Place 4"),
              PlaceBlockWidget("Place 5"),
            ],
          ),
        ],
        backgroundColor: Theme.of(context).accentColor,
        initiallyExpanded: false,
      ),
    );
  }
}
