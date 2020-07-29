import 'package:flutter_test/flutter_test.dart';
import 'package:tripmeout/model/place_visit.dart';
import 'package:tripmeout/services/rest_api_place_visit_service.dart';

void main() {
  group('Populated RestApiPlaceVisitService', () {
    var placeVisitService = RestApiPlaceVisitService(endpoint: 'http://localhost:8080');
    test('listPlaceVisits returns all place visits associated with a trip', () async {
      var placeVisit1 = PlaceVisit(
        name: 'London', 
        tripid: 'abc',
        placesApiPlaceId: 'ChIJVTPokywQkFQRmtVEaUZlJRA',
        userMark: UserMark.MAYBE,
      );
      PlaceVisit createdPlaceVisit1 = await placeVisitService.createPlaceVisit(placeVisit1);
      var placeVisit2 = PlaceVisit(
        name: 'London', 
        tripid: 'abc',
        placesApiPlaceId: 'ChIJVTPokywQkFQRmtVEaUZlJRA',
        userMark: UserMark.YES,
      );
      PlaceVisit createdPlaceVisit2 = await placeVisitService.createPlaceVisit(placeVisit2);
      expect(await placeVisitService.listPlaceVisits('abc'), unorderedEquals([createdPlaceVisit1, createdPlaceVisit2]));
    });

    test('listPlaceVisits returns empty list for invalid tripid', () async {
      var placeVisit1 = PlaceVisit(
        name: 'London', 
        tripid: 'abc123',
        placesApiPlaceId: 'ChIJVTPokywQkFQRmtVEaUZlJRA',
        userMark: UserMark.MAYBE,
      );
      await placeVisitService.createPlaceVisit(placeVisit1);
      var placeVisit2 = PlaceVisit(
        name: 'London', 
        tripid: 'abc123',
        placesApiPlaceId: 'ChIJVTPokywQkFQRmtVEaUZlJRA',
        userMark: UserMark.YES,
      );
      await placeVisitService.createPlaceVisit(placeVisit2);
      expect(await placeVisitService.listPlaceVisits('123abc'), isEmpty);
    });

    test('getPlaceVisit returns placevisit for existing id and tripid', () async {
      var placeVisit = PlaceVisit(
        name: 'London', 
        tripid: 'abc123',
        placesApiPlaceId: 'ChIJVTPokywQkFQRmtVEaUZlJRA',
        userMark: UserMark.MAYBE,
      );
      var createdPlaceVisit = await placeVisitService.createPlaceVisit(placeVisit);

      var expectedPlaceVisit = PlaceVisit(
        id: createdPlaceVisit.id,
        name: 'London', 
        tripid: 'abc123',
        placesApiPlaceId: 'ChIJVTPokywQkFQRmtVEaUZlJRA',
        userMark: UserMark.MAYBE,
      );

      expect(await placeVisitService.getPlaceVisit(createdPlaceVisit.tripid, createdPlaceVisit.id), equals(expectedPlaceVisit));
    });

    test('getPlaceVisit throws error for non-created id', () async {
      var placeVisit = PlaceVisit(
        name: 'London', 
        tripid: 'abc123',
        placesApiPlaceId: 'ChIJVTPokywQkFQRmtVEaUZlJRA',
        userMark: UserMark.MAYBE,
      );
      await placeVisitService.createPlaceVisit(placeVisit);
      expect(() async => await placeVisitService.getPlaceVisit('789', '123'), throwsException);
    });

    test('getPlaceVisit throws error for deleted place visit', () async {
      var placeVisit = PlaceVisit(
        name: 'London', 
        tripid: 'abc123',
        placesApiPlaceId: 'ChIJVTPokywQkFQRmtVEaUZlJRA',
        userMark: UserMark.MAYBE,
      );
      var createdPlaceVisit = await placeVisitService.createPlaceVisit(placeVisit);
      await placeVisitService.deletePlaceVisit(createdPlaceVisit.tripid, createdPlaceVisit.id);
      expect(() async => await placeVisitService.getPlaceVisit(createdPlaceVisit.tripid, createdPlaceVisit.id), throwsException);
    });

    test('updatePlaceVisitUserMark then get returns updated place visit', () async {
      var originalPlaceVisit = PlaceVisit(
        name: 'London', 
        tripid: 'abc123',
        placesApiPlaceId: 'ChIJVTPokywQkFQRmtVEaUZlJRA',
        userMark: UserMark.MAYBE,
      );
      var createdPlaceVisit = await placeVisitService.createPlaceVisit(originalPlaceVisit);

      var newPlaceVisit = PlaceVisit(
        name: 'London', 
        id: createdPlaceVisit.id,
        tripid: 'abc123',
        placesApiPlaceId: 'ChIJVTPokywQkFQRmtVEaUZlJRA',
        userMark: UserMark.YES,
      );
      var updatedPlaceVisit = await placeVisitService.updatePlaceVisitUserMark(newPlaceVisit);

      expect(await placeVisitService.getPlaceVisit(createdPlaceVisit.tripid, createdPlaceVisit.id), equals(updatedPlaceVisit));

    });

  });
}
