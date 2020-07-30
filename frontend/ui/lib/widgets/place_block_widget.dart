import 'package:flutter/material.dart';
import 'package:google_maps/google_maps_places.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/services/place_visit_service.dart';

class PlaceBlockWidget extends StatefulWidget {
  final PlaceVisit placeVisit;
  final PlaceVisitService placeVisitService;
  final PlaceResult details;

  PlaceBlockWidget(this.placeVisit, this.placeVisitService, this.details);

  @override
  State createState() => _PlaceBlockWidgetState(placeVisit, placeVisitService, details);
}

class _PlaceBlockWidgetState extends State<PlaceBlockWidget> {
  final PlaceVisit placeVisit;
  final PlaceVisitService placeVisitService;
  final PlaceResult details;
  _PlaceBlockWidgetState(this.placeVisit, this.placeVisitService, this.details);

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
          Column(children: [
            Row(children: details.types.map((type) => Text(type)).toList()),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Row(children: [
                  Text(details.website), 
                  Text(details.formattedAddress), 
                  Text(details.formattedPhoneNumber)
                ]),
                Row(children: [
                  Row(children: getDollarSigns(details.priceLevel)),
                  Row(children: getStars(details.rating))
                ]),
              ],
            ),
            Image(image: NetworkImage(details.photos[0].getUrl(PhotoOptions())),)
          ])
        ]);
  }

  List<Widget> getDollarSigns(num numSigns) {
    List<Icon> dollarSigns = [];
    while (numSigns > 0) {
      dollarSigns.add(Icon(Icons.attach_money));
      numSigns--;
    }

    return dollarSigns;
  }

  List<Widget> getStars(num numStars) {
    List<Icon> stars = [];
    while (numStars > 0) {
      stars.add(Icon(Icons.star));
      numStars--;
    }

    if (numStars > 0.25 && numStars < 0.75) {
      stars.add(Icon(Icons.star_half));
    } else if (numStars >= 0.75) {
      stars.add(Icon(Icons.star));
    }

    return stars;
  }
}

Widget getPictureWidgets(List<Color> colors) {
  return new ListView(
      scrollDirection: Axis.horizontal,
      children: colors
          .map((item) => new Container(width: 200.0, color: item))
          .toList());
}
