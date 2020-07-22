import 'package:tripmeout/model/trip.dart';
import 'package:http/http.dart';

class TripService {
  Future<List<Trip>> listTrips() async {
    final response = await http.get('/api/trips');
    if (response.statusCodde == 200){
      var asList = json.decode(response.body) as List;
      return asList.map(Trip.fromJson).toList();
    }else{
      throw Exception('Failed to get List of trips');
    }
  }
  Future<Trip> getTrip(String id) async {
     final response = await http.get('/api/trips/$id');
    if (response.statusCodde == 200){
      return Trip.fromJson(json.decode(response.body));
    }else{
       throw Exception('Failed to get Trip');
    }
  }
  Future<Trip> createTrip(Trip trip) async {
    final http.Response response = await http.post('/api/trips',
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{
      'name': trip.name,
      'id': trip.id,
      'locationLat': trip.location.latitude,
      'locationLong':trip.location.longitude
    }),
    );
    if(response.statusCode == 201){
      return Album.fromJson(json.decode(response.body));
    }else{
       throw Exception('Failed to load Trip');
    }
  }
}
