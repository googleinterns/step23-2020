import 'package:flutter/material.dart';
import 'package:tripmeout/model/place.dart';
import 'package:url_launcher/url_launcher.dart';

class ContactInfoWidget extends StatelessWidget {
  PlaceWrapper details;

  ContactInfoWidget(this.details);

  @override
  Widget build(BuildContext context) {
    return Row(children: [
      Padding(
        padding: EdgeInsets.fromLTRB(20.0, 10.0, 20.0, 10.0),
        child: Row(
          children: [
            Text(
              'Website: ',
              style: TextStyle(
                fontWeight: FontWeight.bold,
                color: Theme.of(context).accentColor,
              ),
            ),
            InkWell(
              child: new Text(
                details.website ?? "",
                style: TextStyle(
                  color: Colors.blue,
                ),
              ),
              onTap: () => launch(details.website),
            ),
          ],
        ),
      ),
      Padding(
        padding: EdgeInsets.fromLTRB(20.0, 10.0, 20.0, 10.0),
        child: Row(
          children: [
            Text(
              'Address: ',
              style: TextStyle(
                fontWeight: FontWeight.bold,
                color: Theme.of(context).accentColor,
              ),
            ),
            Text(details.address ?? ""),
          ],
        ),
      ),
      Padding(
        padding: EdgeInsets.fromLTRB(20.0, 10.0, 20.0, 10.0),
        child: Row(
          children: [
            Text(
              'Phone #: ',
              style: TextStyle(
                fontWeight: FontWeight.bold,
                color: Theme.of(context).accentColor,
              ),
            ),
            Text(details.phoneNumber ?? ""),
          ],
        ),
      ),
    ]);
  }
}
