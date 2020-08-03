import 'package:flutter/material.dart';
import 'package:tripmeout/model/place.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/places_services_html.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';

class RecommendedWidgetFromService extends StatelessWidget {
  final TripService tripService;
  final PlacesApiServices placesApiService;
  final String tripId;

  RecommendedWidgetFromService(this.tripService, this.placesApiService, this.tripId);

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: placesApiService.getNearbyPlaces(this.tripService, this.tripId),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return RecommendedWidget(snapshot.data, this.tripId);
        }
        if (snapshot.hasError) {
          Scaffold.of(context).showSnackBar(SnackBar(
            content: Text("Error getting suggested places"),
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

class RecommendedWidget extends StatelessWidget {
  final List<PlaceWrapper> nearbyPlaces;
  final String tripId;

  RecommendedWidget(this.nearbyPlaces, this.tripId);

  @override
  Widget build(BuildContext context) {
    print(nearbyPlaces);
    return CustomScrollView(
      slivers: [
        SliverAppBar(
          pinned: true,
          floating: true,
          snap: true,
          expandedHeight: 250.0,
          automaticallyImplyLeading: false, //Gets rid of appBar
          flexibleSpace: FlexibleSpaceBar(
            title: Text("Suggested Nearby Places"),
            background: Image.network(
              'https://www.gannett-cdn.com/presto/2019/02/01/USAT/2af52e69-3fd1-4438-99d7-487a9b51d03c-GettyImages-878868924.jpg',
              fit: BoxFit.cover,
            ), //Everybackground is seattle
          ),
        ),
        SliverList(
          delegate: SliverChildBuilderDelegate(
            (BuildContext context, int index) {
              return Container(child: PlaceBlockWidget(nearbyPlaces[index].name));
            },
          ),
        ),
      ],
    );
  }
}
