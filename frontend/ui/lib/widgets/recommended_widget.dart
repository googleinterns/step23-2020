import 'package:flutter/material.dart';
import 'package:tripmeout/model/place.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/places_services_html.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';
import 'package:tripmeout/widgets/place_list_widget.dart';
import 'package:tripmeout/router/router.dart';

class RecommendedWidgetFromService extends StatelessWidget {
  final TripService tripService;
  final PlaceVisitService placeVisitService;
  final PlacesApiServices placesApiServices;
  final String tripId;

  RecommendedWidgetFromService(this.tripService, this.placeVisitService,
      this.placesApiServices, this.tripId,
      {Key key})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
        future: tripService.getTrip(tripId),
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            return RecommendedWidget(tripService, placeVisitService,
                placesApiServices, snapshot.data);
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
        });
  }
}

class RecommendedWidget extends StatelessWidget {
  final TripService tripService;
  final PlaceVisitService placeVisitService;
  final PlacesApiServices placesApiService;
  final Trip trip;

  RecommendedWidget(this.tripService, this.placeVisitService,
      this.placesApiService, this.trip);

  Future<List<PlaceBlockWidget>> getNearbyPlaceBlockWidgets() async {
    List<PlaceWrapper> placeWrappers =
        await placesApiService.getNearbyPlaces(tripService, trip.id);
    List<PlaceVisit> placeVisits = placeWrappers
        .map((placeWrapper) => PlaceVisit(
              name: placeWrapper.name,
              tripid: trip.id,
              placesApiPlaceId: placeWrapper.placeId,
            ))
        .toList();

    List<PlaceBlockWidget> placeBlocks = [];
    for (int i = 0; i < placeWrappers.length; i++) {
      placeBlocks.add(PlaceBlockWidget(
          placeVisitService, placeVisits[i], placeWrappers[i]));
    }

    return placeBlocks;
  }

  @override
  Widget build(BuildContext context) {
    return PlaceListFromServiceWidget(
        trip, 'Recommended Places To Visit', getNearbyPlaceBlockWidgets);
  }
}
