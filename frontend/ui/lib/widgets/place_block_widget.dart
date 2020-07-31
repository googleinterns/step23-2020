import 'package:flutter/material.dart';
import 'package:google_maps/google_maps_places.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:url_launcher/url_launcher.dart';

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
    print(placeVisit.id);
    print(placeVisit.tripid);

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
            Padding(
              padding: EdgeInsets.only(
                left: 20.0,
                right: 20.0,
                bottom: 10.0
              ),
              child: Row(children: formatTypes(details.types)),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Row(children: [
                  Padding(
                    padding: EdgeInsets.only(
                      left: 20.0,
                      right: 20.0,
                      top: 10.0,
                      bottom: 10.0
                    ),
                    child: Row(
                      children: [
                        Text(
                          'Website: ',
                          style: TextStyle(
                            fontWeight: FontWeight.bold, 
                            color: Theme.of(context).accentColor
                          ),
                        ),
                        InkWell(
                          child: new Text(
                            details.website ?? "",
                            style: TextStyle(
                              color: Colors.blue,
                            ),
                          ),
                          onTap: () => launch(details.website)
                        ),
                      ],
                    ),
                  ), 
                  Padding(
                    padding: EdgeInsets.only(
                      left: 20.0,
                      right: 20.0,
                      top: 10.0,
                      bottom: 10.0
                    ),
                    child: Row(
                      children: [
                        Text(
                          'Address: ',
                          style: TextStyle(
                            fontWeight: FontWeight.bold, 
                            color: Theme.of(context).accentColor
                          ),
                        ),
                        Text(details.formattedAddress ?? ""),
                      ],
                    ),
                  ),
                  Padding(
                    padding: EdgeInsets.only(
                      left: 20.0,
                      right: 20.0,
                      top: 10.0,
                      bottom: 10.0
                    ),
                    child: Row(
                      children: [
                        Text(
                          'Phone #: ',
                          style: TextStyle(
                            fontWeight: FontWeight.bold, 
                            color: Theme.of(context).accentColor
                          ),
                        ),
                        Text(details.formattedPhoneNumber ?? ""),
                      ],
                    ),
                  ),
                ]),
                Row(children: [
                  Padding(
                    padding: EdgeInsets.only(
                      left: 20.0,
                      right: 20.0,
                      top: 10.0,
                      bottom: 10.0
                    ),
                    child: Row(children: getDollarSigns(details.priceLevel))
                  ),
                  Padding(
                    padding: EdgeInsets.only(
                      left: 20.0,
                      right: 20.0,
                      top: 10.0,
                      bottom: 10.0
                    ),
                    child: Row(children: getStars(details.rating))
                  ),
                ]),
              ],
            ),
            Padding(
              padding: const EdgeInsets.all(25.0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: getPhotos(details.photos)
              ),
            ),
          ])
        ]);
  }

  List<Widget> getDollarSigns(num numSigns) {
    if (numSigns == null) {
      return [Text('no price info found')];
    }
    List<Icon> dollarSigns = [];
    while (numSigns > 0) {
      dollarSigns.add(Icon(Icons.attach_money, color: Colors.green));
      numSigns--;
    }
    return dollarSigns;
  }

  List<Widget> getStars(num numStars) {
    if (numStars == null) {
      return [Text('no ratings found')];
    }
    int totalStars = 5;
    List<Icon> stars = [];
    while (numStars > 1) {
      stars.add(Icon(Icons.star, color: Colors.amber));
      numStars--;
      totalStars--;
    }
    if (numStars > 0.25 && numStars < 0.75) {
      stars.add(Icon(Icons.star_half, color: Colors.amber));
      totalStars--;
    } else if (numStars >= 0.75) {
      stars.add(Icon(Icons.star, color: Colors.amber));
      totalStars--;
    }

    while(totalStars > 0) {
      stars.add(Icon(Icons.star_border, color: Colors.amber));
      totalStars--;
    }

    return stars;
  }

  List<Widget> formatTypes(List<String> types) {
    List<Text> formattedTypes = [];
    for (int i = 0; i < types.length; i++) {
      String type;
      if (i == types.length - 1) {
        type = types[i];
      } else {
        type = types[i] + ", ";
      }
      formattedTypes.add(Text(
        type, 
        style: TextStyle(
          fontStyle: FontStyle.italic),
      ));
    }
    return formattedTypes;
  }

  List<Widget> getPhotos(List<PlacePhoto> photos) {
    List<Image> imageWidgets = [];
    PhotoOptions photoOptions = PhotoOptions()
                ..maxHeight = 400
                ..maxWidth = 400;
    for (int i = 0; i < photos.length && i < 3; i++) {
      imageWidgets.add(Image(image: NetworkImage(photos[i].getUrl(photoOptions))));
    }
    return imageWidgets;
  }
}

Widget getPictureWidgets(List<Color> colors) {
  return new ListView(
      scrollDirection: Axis.horizontal,
      children: colors
          .map((item) => new Container(width: 200.0, color: item))
          .toList());
}
