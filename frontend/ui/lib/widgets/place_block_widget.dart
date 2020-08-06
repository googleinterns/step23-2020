import 'package:flutter/material.dart';
import 'package:tripmeout/model/place.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/widgets/place_details_widget.dart';
import 'package:tripmeout/widgets/trip_view_user_status_widget.dart';
import 'package:tripmeout/widgets/recommended_user_status_widget.dart';
import 'package:tripmeout/services/place_visit_service.dart';

class PlaceBlockWidget extends StatefulWidget {
  final PlaceVisit placeVisit;
  final PlaceVisitService placeVisitService;
  final PlaceWrapper details;
  final bool showTripViewUserStatus;

  PlaceBlockWidget(this.placeVisitService, this.placeVisit, this.details, this.showTripViewUserStatus);

  @override
  State createState() =>
      _PlaceBlockWidgetState(placeVisit, placeVisitService, details, showTripViewUserStatus);
}

class _PlaceBlockWidgetState extends State<PlaceBlockWidget> {
  final PlaceVisit placeVisit;
  final PlaceVisitService placeVisitService;
  final PlaceWrapper details;
  final bool showTripViewUserStatus;
  _PlaceBlockWidgetState(this.placeVisit, this.placeVisitService, this.details, this.showTripViewUserStatus);

  @override
  Widget build(BuildContext context) {
    return ExpansionTile(
        initiallyExpanded: false,
        title: Text(placeVisit.name),
        trailing: Container(
          width: 100.0,
          child: showTripViewUserStatus ? UserStatusWidget(placeVisitService, placeVisit) : RecommendedUserStatusWidget(placeVisitService, placeVisit),
        ),
        children: [PlaceDetailsWidget(details)]);
  }
}
