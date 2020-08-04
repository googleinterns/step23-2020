import 'package:flutter/material.dart';
import 'package:tripmeout/model/place.dart';
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

  TripViewWidgetFromService(this.tripService, this.placeVisitService,
      this.placesApiServices, this.tripId);

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: tripService.getTrip(tripId),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return PlaceListWidget(
              snapshot.data, placeVisitService, placesApiServices);
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

class PlaceListWidget extends StatelessWidget {
  final Trip trip;
  final PlaceVisitService placeVisitService;
  final PlacesApiServices placesApiServices;

  PlaceListWidget(this.trip, this.placeVisitService, this.placesApiServices);

  Future<List<PlaceBlockWidget>> getPlaceBlockWidgets(String tripid) async {
    //List<PlaceVisit> placeVisits =
    //await placeVisitService.listPlaceVisits(tripid);
    // test place visit since currently cannot create

    PlaceVisit seattle = PlaceVisit(
        name: 'Seattle',
        id: 'hello world',
        tripid: 'abc123',
        placesApiPlaceId: 'ChIJ3S-JXmauEmsRUcIaWtf4MzE',
        userMark: UserMark.YES);
    List<PlaceVisit> placeVisits = [seattle];

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
      future: getPlaceBlockWidgets(trip.id),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return TripViewWidget(trip, snapshot.data);
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
  final List<PlaceBlockWidget> placeBlocks;

  TripViewWidget(this.trip, this.placeBlocks);

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
              return placeBlocks[index];
            },
          ),
        ),
      ],
    );
  }
}
