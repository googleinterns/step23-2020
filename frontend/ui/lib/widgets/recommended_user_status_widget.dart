import 'package:flutter/material.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/widgets/alert_banner_widget.dart';
import 'package:tripmeout/router/router.dart';

class RecommendedUserStatusWidget extends StatefulWidget {
  PlaceVisitService placeVisitService;
  PlaceVisit placeVisit;

  RecommendedUserStatusWidget(this.placeVisitService, this.placeVisit);

  @override
  _RecommendedUserStatusState createState() =>
      _RecommendedUserStatusState(this.placeVisitService, this.placeVisit);
}

class _RecommendedUserStatusState extends State<RecommendedUserStatusWidget> {

  PlaceVisitService placeVisitService;
  PlaceVisit placeVisit;
  PlaceVisit _placeVisit;

  _RecommendedUserStatusState(this.placeVisitService, this.placeVisit)
      : _placeVisit = placeVisit;

  bool _must_selected = false;
  bool _maybe_selected = false;
  Icon _must_icon = Icon(Icons.favorite_border);
  Icon _maybe_icon = Icon(Icons.bookmark_border);
  Color _must_color = Colors.black;
  Color _maybe_color = Colors.black;

  @override
  Widget build(BuildContext build) {
    return Row(
      children: [
        IconButton(
          icon: _must_icon,
          onPressed: () {
            setState(() async {
              if (_must_selected == true) {
                _must_selected = false;
                _must_icon = Icon(Icons.favorite_border);
                _must_color = Colors.black;
                placeVisitService.deletePlaceVisit(_placeVisit.tripid, _placeVisit.id);
              } else {
                _must_selected = true;
                _must_icon = Icon(Icons.favorite);
                _must_color = Colors.pink;

                _maybe_selected = false;     
                _maybe_icon = Icon(Icons.bookmark_border);
                _maybe_color = Colors.black;
                _placeVisit =
                    PlaceVisit.from(_placeVisit, userMark: UserMark.YES);
                _placeVisit = await placeVisitService.updatePlaceVisitUserMark(_placeVisit);
              }
            });
          },
          color: _must_color,
          tooltip: "Must Go",
        ),
        IconButton(
          icon: _maybe_icon,
          onPressed: () {
            setState(() async {
              if (_maybe_selected == true) {
                _maybe_selected = false;
                _maybe_icon = Icon(Icons.bookmark_border);
                _maybe_color = Colors.black;
                placeVisitService.deletePlaceVisit(_placeVisit.tripid, _placeVisit.id);
              } else {
                _maybe_selected = true;
                _maybe_icon = Icon(Icons.bookmark);
                _maybe_color = Colors.red;

                _must_selected = false;
                _must_icon = Icon(Icons.favorite_border);
                _must_color = Colors.black;
                _placeVisit =
                    PlaceVisit.from(_placeVisit, userMark: UserMark.MAYBE);
                _placeVisit = await placeVisitService.updatePlaceVisitUserMark(_placeVisit);
              }
            });
          },
          color: _maybe_color,
          tooltip: "Save Place",
        ),
      ],
    );
  }
}
