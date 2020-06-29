async function fetchAndSetTrips() {
  let trips = await fetchTrips();
  setTripListPage(trips);
}


function fetchTrips() {
  return new Promise((resolve, reject) => {
    let trips = [
      {'name': 'Uk trip'}, {'name': 'Bora Bora trip'}, {'name': 'Mexico trip'},
      {'name': 'Japan trip'}, {'name': 'European trip'}
    ];
    resolve(trips);
  });
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
