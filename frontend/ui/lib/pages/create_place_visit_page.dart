import 'package:tripmeout/pages/basic_page.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/widgets/create_place_visit_widget.dart';
import 'package:tripmeout/widgets/default_app_bar.dart';
import 'package:flutter/material.dart';

class CreatePlaceVisitPage extends StatelessWidget {
  final String tripId;
  final PlaceVisitService placeVisitService;
  final PlacesApiServices placesApiServices;

  CreatePlaceVisitPage(
      this.placeVisitService, this.placesApiServices, this.tripId,
      {Key key})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return BasicPage(
      child: CreatePlaceVisitWidget(
              this.placeVisitService, this.placesApiServices, this.tripId, key: GlobalKey(),),
    );
  }
}
