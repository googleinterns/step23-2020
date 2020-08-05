import 'package:flutter/material.dart';
import 'package:tripmeout/model/place.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/widgets/place_details_widget.dart';
import 'package:tripmeout/widgets/user_status_widget.dart';
import 'package:tripmeout/services/place_visit_service.dart';

class PlaceBlockWidget extends StatefulWidget {
  final PlaceVisit placeVisit;
  final PlaceVisitService placeVisitService;
  final PlaceWrapper details;

  PlaceBlockWidget(this.placeVisitService, this.placeVisit, this.details);

  @override
  State createState() =>
      _PlaceBlockWidgetState(placeVisit, placeVisitService, details);
}

class _PlaceBlockWidgetState extends State<PlaceBlockWidget> {
  final PlaceVisit placeVisit;
  final PlaceVisitService placeVisitService;
  final PlaceWrapper details;
  _PlaceBlockWidgetState(this.placeVisit, this.placeVisitService, this.details);

  @override
  Widget build(BuildContext context) {
    return ExpansionTile(
      initiallyExpanded: false,
      title: Text(placeVisit.name),
      trailing: Container(
        width: 100.0,
        child: UserStatusWidget(placeVisitService, placeVisit),
      ),
      children: [PlaceDetailsWidget(details)]
    );
  }
}
