import 'package:flutter/material.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';

class TripViewWidget extends StatefulWidget {
  TripViewWidget({Key key}) : super(key: key);
  TripWidgetState createState() => TripWidgetState();
}

class TripWidgetState extends State<TripViewWidget> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Container(
        padding: const EdgeInsets.all(10.0),
        width: MediaQuery.of(context).size.width * .80,
        child: ExpansionTile(
          title: Text("Trip Name"),
          trailing: Text("M T D"),
          children: <Widget>[
            Column(
              children: <Widget>[
                PlaceBlockWidget("Place 1"),
                PlaceBlockWidget("Place 2"),
                PlaceBlockWidget("Place 3"),
                PlaceBlockWidget("Place 4"),
                PlaceBlockWidget("Place 5"),
              ],
            ),
          ],
          backgroundColor: Colors.green,
          initiallyExpanded: false,
        ),
      ),
    );
  }
}
