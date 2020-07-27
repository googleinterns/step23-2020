import 'package:flutter/material.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/place_block_widget.dart';

class TripViewWidget extends StatefulWidget {
  final TripService tripService;
  final String tripId;

  TripViewWidget(this.tripService, this.tripId);
  _TripWidgetState createState() => _TripWidgetState(this.tripId);
}

class _TripWidgetState extends State<TripViewWidget> {
  TripService get tripService => widget.tripService;

  final String tripId;

  _TripWidgetState(this.tripId);

  Trip _tripForPage;
  String placeHolder;

  @override
  void initState() {
    getTripFuture(tripId).then((trip) {
      setState(() {
        _tripForPage = trip;
        placeHolder = _tripForPage.name;
      });
    }, onError: (error) {
      placeHolder = "hello world";
      print(error);
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
          border: Border.all(
            color: Colors.black,
          ),
          borderRadius: BorderRadius.all(Radius.circular(5))),
      margin: const EdgeInsets.all(15.0),
      padding: const EdgeInsets.all(10.0),
      child: Column(
        children: [
          Text(placeHolder),
          PlaceBlockWidget("Place 1"),
          PlaceBlockWidget("Place 2"),
          PlaceBlockWidget("Place 3"),
          PlaceBlockWidget("Place 4"),
          PlaceBlockWidget("Place 5"),
        ],
      ),
    );
  }

  Future<Trip> getTripFuture(tripId) async {
    try {final trip = await tripService.getTrip(tripId);
    return trip;
    } catch (err){
    return Future.error('Error getting $tripId');
    }
  }
}
