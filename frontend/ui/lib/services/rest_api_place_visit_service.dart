import 'dart:convert';
import 'package:tripmeout/model/place_visit.dart';
import 'package:http/http.dart' as http;
import 'package:tripmeout/services/place_visit_service.dart';

class RestApiPlaceVisitService implements PlaceVisitService {
  final String endpoint;

  RestApiPlaceVisitService({this.endpoint = ''});

  Future<List<PlaceVisit>> listPlaceVisits(String tripid) async {
    final response = await http.get('$endpoint/api/trips/$tripid/placeVisits');
    if (response.statusCode == 200) {
      var asList =
          (json.decode(response.body) as List).cast<Map<String, dynamic>>();
      return asList.map((x) => PlaceVisit.fromJson(x)).toList();
    } else {
      throw Exception('Failed to get List of PlaceVisits with status code ${response.statusCode}');
    }
  }

  Future<PlaceVisit> getPlaceVisit(String tripid, String id) async {
    final response = await http.get('$endpoint/api/trips/$tripid/placeVisits/$id');
    if (response.statusCode == 200) {
      return PlaceVisit.fromJson(json.decode(response.body));
    } else {
      throw Exception('Failed to get PlaceVisit with status code ${response.statusCode}');
    }
  }

  Future<PlaceVisit> createPlaceVisit(PlaceVisit placeVisit) async {
    String tripid = placeVisit.tripid;
    final http.Response response = await http.post(
      '$endpoint/api/trips/$tripid/placeVisits',
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, dynamic>{
        'placeName': placeVisit.name,
        'placesApiPlaceId': placeVisit.placesApiPlaceId,
        'userMark': PlaceVisit.userMarkToString(placeVisit.userMark),
      }),
    );
    if (response.statusCode == 201) {
      return PlaceVisit.fromJson(json.decode(response.body));
    } else {
      throw Exception('Failed to load PlaceVisit with status code ${response.statusCode}');
    }
  }

  Future<PlaceVisit> updatePlaceVisitUserMark(PlaceVisit placeVisit) async {
    String tripid = placeVisit.tripid;
    String id = placeVisit.id;
    final http.Response response = await http.put(
      '$endpoint/api/trips/$tripid/placeVisits/$id',
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, dynamic>{
        'placeName': placeVisit.name,
        'placesApiPlaceId': placeVisit.placesApiPlaceId,
        'userMark': PlaceVisit.userMarkToString(placeVisit.userMark),
      }),
    );
    if (response.statusCode == 200) {
      return PlaceVisit.fromJson(json.decode(response.body));
    } else {
      throw Exception('Failed to update PlaceVisit with status code ${response.statusCode}');
    }
  }

  Future<Null> deletePlaceVisit(String tripid, String id) async {
    final response = await http.delete('$endpoint/api/trips/$tripid/placeVisits/$id');

    if (response.statusCode != 200) {
      throw Exception('Failed to delete PlaceVisit $id with status code ${response.statusCode}');
    }
  }
}
