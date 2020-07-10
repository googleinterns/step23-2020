import 'package:flutter/material.dart';

class CreateTripWidget extends StatefulWidget {
  CreateTripWidget({Key key}) : super(key: key);
  @override
  CreateTripState createState() => CreateTripState();
}

class CreateTripState extends State<CreateTripWidget> {
  final _formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return Form(
      key: _formKey,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          TextFormField(
            decoration: const InputDecoration(
              hintText: 'Please enter Location',
            ),
            validator: (value) {
              if (value.isEmpty) {
                return 'Please enter some text';
              }
              return null;
            },
          ),
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 16.0),
            child: RaisedButton(
              onPressed: () {
                if (_formKey.currentState.validate()) {}
              },
              child: Text('Submit'),
            ),
          ),
        ],
      ),
    );
  }
}
