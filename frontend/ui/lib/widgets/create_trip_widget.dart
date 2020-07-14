import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:tripmeout/widgets/map_widget.dart';

class CreateTripWidget extends StatefulWidget {
  @override
  _CreateTripWidgetState createState() => _CreateTripWidgetState();
}

class _CreateTripWidgetState extends State<CreateTripWidget> {
  String place = "No Input";
  int radius = 0;
  String newInformation = 'Grabbed info placed here.';
  changeText() {
    setState(() {
      newInformation = place + " " + radius.toString() + " km";
    });
  }

  @override
  Widget build(BuildContext context) {
    return Center(
        child: FittedBox(
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(25.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Container(
                  width: 250.0,
                  child: TextFormField(
                    onChanged: (text) {
                      place = text;
                    },
                    textAlign: TextAlign.center,
                    decoration: InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Enter your Location',
                    ),
                  ),
                ),
                Container(
                  width: 125.0,
                  child: TextFormField(
                    onChanged: (text) {
                      radius = int.parse(text);
                    },
                    textAlign: TextAlign.center,
                    keyboardType: TextInputType.number,
                    inputFormatters: <TextInputFormatter>[
                      WhitelistingTextInputFormatter.digitsOnly
                    ],
                    decoration: InputDecoration(
                        border: OutlineInputBorder(), labelText: 'Radius KM'),
                  ),
                ),
              ],
            ),
          ),
          Row(
            children: [
              Padding(
                padding: const EdgeInsets.all(25.0),
                child: MapWidget(),
              ),
            ],
          ),
          Row(
            children: [
              Container(
                  child:
                      Text('$newInformation', style: TextStyle(fontSize: 21))),
            ],
          ),
          Row(
            children: [
              Padding(
                padding: const EdgeInsets.all(25.0),
                child: RaisedButton(
                  onPressed: () => changeText(),
                  child: Text('Submit'),
                  textColor: Colors.white,
                  color: Colors.black,
                ),
              ),
            ],
          )
        ],
      ),
      fit: BoxFit.contain,
    ));
  }
}
