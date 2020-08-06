import 'package:flutter/material.dart';
import 'package:tripmeout/model/place.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/place_visit_service.dart';
import 'package:tripmeout/services/places_services.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';

typedef _OnLoad = Future<List<PlaceBlockWidget>> Function();

class PlaceListFromServiceWidget extends StatelessWidget {
  final Trip trip;
  final String pageName;
  final PlacesApiServices placesApiServices;
  final _OnLoad getPlaceBlockWidgets;

  PlaceListFromServiceWidget(
      this.trip, this.pageName, this.placesApiServices, this.getPlaceBlockWidgets);

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: getPlaceBlockWidgets.call(),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return PlaceListWidgetWithImage(trip, pageName, placesApiServices, snapshot.data);
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

class PlaceListWidgetWithImage extends StatelessWidget {
  final Trip trip;
  final String pageName;
  final PlacesApiServices placesApiServices;
  final List<PlaceBlockWidget> placeBlocks;

  PlaceListWidgetWithImage(this.trip, this.pageName, this.placesApiServices, this.placeBlocks);

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: placesApiServices.getGoodBannerPhoto(trip.placesApiPlaceId),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          if (snapshot.data == null) {
            return PlaceListWidget(trip, pageName, 'https://www.gannett-cdn.com/presto/2019/02/01/USAT/2af52e69-3fd1-4438-99d7-487a9b51d03c-GettyImages-878868924.jpg', placeBlocks);
          }
          return PlaceListWidget(trip, pageName, snapshot.data, placeBlocks);
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
  final String pageName;
  final String photoUrl;
  final List<PlaceBlockWidget> placeBlocks;

  PlaceListWidget(this.trip, this.pageName, this.photoUrl, this.placeBlocks);

  @override
  Widget build(BuildContext context) {
    print(photoUrl);
    return CustomScrollView(
      slivers: [
        SliverAppBar(
          pinned: true,
          floating: true,
          snap: true,
          expandedHeight: 250.0,
          automaticallyImplyLeading: false, //Gets rid of appBar back arrow
          flexibleSpace: FlexibleSpaceBar(
            title: Text(pageName),
            background: Image.network(
              photoUrl,
              fit: BoxFit.cover,
            ), //Everybackground is seattle
          ),
          SliverList(
            delegate: SliverChildBuilderDelegate(
              (BuildContext context, int index) {
                if (index < placeBlocks.length) {
                  return placeBlocks[index];
                }
                return null;
              },
            ),
          ),
        ],
      ),
    );
  }
}
