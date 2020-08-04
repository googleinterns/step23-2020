import 'package:flutter/material.dart';
import 'package:tripmeout/model/trip.dart';
import 'package:tripmeout/router/router.dart';
import 'package:tripmeout/services/trip_service.dart';
import 'package:tripmeout/widgets/alert_banner_widget.dart';

class TripDeleterButton extends StatelessWidget {
  final TripService tripService;
  final Trip trip;

  TripDeleterButton(this.tripService, this.trip);

  @override
  Widget build(BuildContext context) {
    return IconButton(
      onPressed: () {
        _showDialog(context, trip, tripService);
      },
      icon: Icon(Icons.delete),
      color: Colors.red,
      tooltip: 'Click here to delete trip.',
    );
  }

  _showDialog(BuildContext context, Trip trip, TripService tripService) {
    VoidCallback continueCallBack = () => {
          tripService.deleteTrip(trip.id),
          Navigator.pushNamed(context, Router.tripListRoute),
        };
    AlertBannerWidget alert = AlertBannerWidget("Delete Trip",
        "Are you sure you would like to delete this trip?", continueCallBack);

    showDialog(
      context: context,
      builder: (BuildContext context) {
        return alert;
      },
    );
  }
}
