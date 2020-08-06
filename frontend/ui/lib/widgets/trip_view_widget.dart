import 'package:flutter/material.dart';
import 'package:tripmeout/model/place.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/place_list_widget.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';

class TripViewWidgetFromService extends StatelessWidget {
  final TripService tripService;
  final PlaceVisitService placeVisitService;
  final PlacesApiServices placesApiServices;
  final String tripId;

  TripViewWidgetFromService(this.tripService, this.placeVisitService,
      this.placesApiServices, this.tripId,
      {Key key})
      : super(key: key);

  Future<List<PlaceBlockWidget>> getPlaceBlockWidgets() async {
    List<PlaceVisit> placeVisits =
        await placeVisitService.listPlaceVisits(tripId);
    List<PlaceBlockWidget> placeBlockWidgets = [];
    for (int i = 0; i < placeVisits.length; i++) {
      PlaceVisit placeVisit = placeVisits[i];
      PlaceWrapper placeDetails =
          await placesApiServices.getPlaceDetails(placeVisit.placesApiPlaceId);
      placeBlockWidgets
          .add(PlaceBlockWidget(placeVisitService, placeVisit, placeDetails));
    }
    return placeBlockWidgets;
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: tripService.getTrip(tripId),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return PlaceListFromServiceWidget(
              snapshot.data, snapshot.data.name, placesApiServices, getPlaceBlockWidgets);
        }
        if (snapshot.hasError) {
          Scaffold.of(context).showSnackBar(SnackBar(
            content: Text("Error getting trip"),
            action: SnackBarAction(
              label: "Retry",
              onPressed: () {}, //TODO: Make retry button actually work.
            ),
          ));
          return Container();
        }
        return CircularProgressIndicator();
      },
    );
  }
}
