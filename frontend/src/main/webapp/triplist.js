function tripData() {
  var trips = [
    {'name': 'Uk trip'}, {'name': 'Bora Bora trip'}, {'name': 'Mexico trip'},
    {'name': 'Japan trip'}, {'name': 'European trip'}
  ];
  setTripListPage(trips);
}

function setTripListPage(trips) {
  const tripList = document.getElementById('trip-list');
  trips.forEach((trip) => {
    tripList.appendChild(createTripElement(trip));
  })
}

function createTripElement(trip) {
  const tripElement = document.createElement('li');
  tripElement.innerHTML = trip.name;
  return tripElement;
}
