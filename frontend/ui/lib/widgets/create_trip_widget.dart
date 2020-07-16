import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:tripmeout/widgets/map_widget.dart';
import 'package:tripmeout/themes/default_theme.dart';

//TODO: Add loading screen after to the Create Trip Widget
//TODO: Get rid of the newInformation text on the page.

class CreateTripWidget extends StatefulWidget {
  @override
  _CreateTripWidgetState createState() => _CreateTripWidgetState();
}

class _CreateTripWidgetState extends State<CreateTripWidget> {
  String place = "No Input";
  int radius = 0;
  String newInformation = 'Grabbed info placed here.';
  void changeText() {
    setState(() {
      newInformation = "$place ${radius}km";
    });
  }

  @override
  Widget build(BuildContext context) {
    return FittedBox(
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
          Padding(
            padding: const EdgeInsets.all(25.0),
            child: MapWidget(),
          ),
          Container(
              child: Text('$newInformation')),
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
      ),
      fit: BoxFit.contain,
    );
  }
}
