import 'package:tripmeout/model/place_visit.dart';

class PlaceVisitService {
  Future<List<PlaceVisit>> listPlaceVisits(String tripid) async {}
  Future<PlaceVisit> getPlaceVisit(String tripid, String id) async {}
  Future<PlaceVisit> createPlaceVisit(PlaceVisit placeVisit) async {}
  Future<Null> deletePlaceVisit(String tripid, String id) async {}
  Future<PlaceVisit> updatePlaceVisitUserMark(PlaceVisit placeVisit) async {}
}
