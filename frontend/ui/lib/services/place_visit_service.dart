import 'package:tripmeout/model/place_visit.dart';

abstract class PlaceVisitService {
  Future<List<PlaceVisit>> listPlaceVisits(String tripid);
  Future<PlaceVisit> getPlaceVisit(String tripid, String id);
  Future<PlaceVisit> createPlaceVisit(PlaceVisit placeVisit);
  Future<void> deletePlaceVisit(String tripid, String id);
  Future<PlaceVisit> updatePlaceVisitUserMark(PlaceVisit placeVisit);
}
