import 'package:flutter/material.dart';
import 'package:google_maps/google_maps_places.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';

class TripViewWidgetFromService extends StatelessWidget {
  final TripService tripService;
  final PlaceVisitService placeVisitService;
  final PlacesApiServices placesApiServices;
  final String tripId;

  TripViewWidgetFromService(this.tripService, this.placeVisitService, this.placesApiServices, this.tripId);

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: tripService.getTrip(tripId),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return TripViewWidget(snapshot.data, placeVisitService, placesApiServices);
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

class TripViewWidget extends StatelessWidget {
  final Trip trip;
  final PlaceVisitService placeVisitService;
  final PlacesApiServices placesApiServices;

  Future<List<PlaceBlockWidget>> places;

  TripViewWidget(this.trip, this.placeVisitService, this.placesApiServices);

  @override
  Widget build(BuildContext context) {
    return CustomScrollView(
      slivers: [
        SliverAppBar(
          pinned: true,
          floating: true,
          snap: true,
          expandedHeight: 250.0,
          automaticallyImplyLeading: false, //Gets rid of appBar back arrow
          flexibleSpace: FlexibleSpaceBar(
            title: Text(trip.name),
            background: Image.network(
              'https://www.gannett-cdn.com/presto/2019/02/01/USAT/2af52e69-3fd1-4438-99d7-487a9b51d03c-GettyImages-878868924.jpg',
              fit: BoxFit.cover,
            ), //Everybackground is seattle
          ),
        ),
        SliverList(
          delegate: SliverChildBuilderDelegate(
            (BuildContext context, int index) {
              if (index == 5) return null;
              return Container(child: PlaceBlockWidget('Place ${index + 1}'));
            },
          ),
        ),
      ],
    );
  }

  Future<List<PlaceBlockWidget>> getPlaceBlockWidgets(String tripid) async {
    List<PlaceVisit> placeVisits = await placeVisitService.listPlaceVisits(tripid);

    List<PlaceBlockWidget> placeBlockWidgets = [];
    for (int i = 0; i < placeVisits.length; i++) {
      PlaceVisit placeVisit = placeVisits[i];
      PlaceResult placeDetails = await placesApiServices.getPlaceDetails(placeVisit.placesApiPlaceId);
      placeBlockWidgets.add(PlaceBlockWidget(placeVisit, placeVisitService, placeDetails));
    }
    return placeBlockWidgets;
  }
}
