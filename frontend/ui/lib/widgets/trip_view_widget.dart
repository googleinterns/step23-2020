import 'package:flutter/material.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';

class TripViewWidgetFromService extends StatelessWidget {
  final TripService tripService;
  final PlaceVisitService placeVisitService;
  final String tripId;

  TripViewWidgetFromService(this.tripService, this.placeVisitService, this.tripId);

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: tripService.getTrip(tripId),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return TripViewWidget(snapshot.data);
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

  TripViewWidget(this.trip);

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
}
