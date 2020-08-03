import 'dart:convert';
import 'package:tripmeout/model/trip.dart';
import 'package:http/http.dart' as http;
import 'package:tripmeout/services/trip_service.dart';

class RestApiTripService implements TripService {
  static final String CONTENT_TYPE = 'application/json; charset=utf-8';
  final String endpoint;

  RestApiTripService({this.endpoint = ''});

  @override
  Future<List<Trip>> listTrips() async {
    final response = await http.get('$endpoint/api/trips');
    if (response.statusCode == 200) {
      var asList =
          (json.decode(response.body) as List).cast<Map<String, dynamic>>();
      return asList.map((x) => Trip.fromJson(x)).toList();
    } else {
      throw Exception('Failed to get List of trips');
    }
  }

  @override
  Future<Trip> getTrip(String id) async {
    final response = await http.get('$endpoint/api/trips/$id');
    if (response.statusCode == 200) {
      return Trip.fromJson(json.decode(response.body));
    } else {
      throw Exception('Failed to get Trip');
    }
  }

  @override
  Future<Trip> createTrip(Trip trip) async {
    final http.Response response = await http.post(
      '$endpoint/api/trips',
      headers: <String, String>{
        'Content-Type': CONTENT_TYPE,
      },
      body: jsonEncode(<String, dynamic>{
        'name': trip.name,
        'placesApiPlaceId': trip.placesApiPlaceId,
      }),
    );
    if (response.statusCode == 200) {
      return Trip.fromJson(json.decode(response.body));
    } else {
      throw Exception('Failed to load Trip');
    }
  }

  @override
  Future<void> deleteTrip(String id) async {
    final http.Response response = await http.delete('$endpoint/api/trips/$id');
    if (response.statusCode != 200) {
      throw Exception('failed to delete trip');
    }
  }
}
