import 'package:flutter/material.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/widgets/alert_banner_widget.dart';
import 'package:tripmeout/router/router.dart';

class UserStatusWidget extends StatefulWidget {
  PlaceVisitService placeVisitService;
  PlaceVisit placeVisit;

  UserStatusWidget(this.placeVisitService, this.placeVisit);

  @override
  _UserStatusState createState() =>
      _UserStatusState(this.placeVisitService, this.placeVisit);
}

class _UserStatusState extends State<UserStatusWidget> {
  //PlaceVisitService get placeVisitService => widget.placeVisitService;
  //PlaceVisit get placeVisit => widget.placeVisit;

  PlaceVisitService placeVisitService;
  PlaceVisit placeVisit;
  PlaceVisit _placeVisit;

  _UserStatusState(this.placeVisitService, this.placeVisit)
      : _placeVisit = placeVisit;

  bool _selected = false;
  Icon _icon = Icon(Icons.favorite_border);
  Color _color = Colors.black;

  _showDialog(BuildContext context, PlaceVisit placeVisit,
      PlaceVisitService placeVisitService) {
    VoidCallback continueCallBack = () => {
          placeVisitService
              .deletePlaceVisit(placeVisit.tripid, placeVisit.id)
              .then((_) {
            Navigator.popAndPushNamed(
                context, Router.createTripViewRoute(placeVisit.tripid));
          }),
        };
    AlertBannerWidget alert = AlertBannerWidget("Delete Place",
        "Are you sure you would like to delete this place?", continueCallBack);

    showDialog(
      context: context,
      builder: (BuildContext context) {
        return alert;
      },
    );
  }

  @override
  Widget build(BuildContext build) {
    if (_placeVisit.userMark == UserMark.YES) {
      setState(() {
        _selected = true;
        _icon = Icon(Icons.favorite);
        _color = Colors.pink;
      });
    }

    return Row(
      children: [
        IconButton(
          icon: _icon,
          onPressed: () {
            setState(() {
              if (_selected == true) {
                _selected = false;
                _icon = Icon(Icons.favorite_border);
                _color = Colors.black;
                _placeVisit =
                    PlaceVisit.from(placeVisit, userMark: UserMark.MAYBE);
              } else {
                _selected = true;
                _icon = Icon(Icons.favorite);
                _color = Colors.pink;
                _placeVisit =
                    PlaceVisit.from(placeVisit, userMark: UserMark.YES);
              }
              placeVisitService.updatePlaceVisitUserMark(_placeVisit);
            });
          },
          color: _color,
          tooltip: "Must Go",
        ),
        IconButton(
          onPressed: () => _showDialog(context, placeVisit, placeVisitService),
          icon: Icon(Icons.delete),
          color: Colors.red,
          tooltip: "Delete this place",
        ),
      ],
    );
  }
}
