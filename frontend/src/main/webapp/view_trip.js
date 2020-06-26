
async function fetchAndSetTripPlace() {
  const eventsList = await fetchPlace();
  eventsList.forEach(createPlacesElement);
}

function fetchPlace() {
  return new Promise((resolve, reject) => {
    let randomEvent = [
      {'name': 'Random Event', 'description': 'This is the description'},
      {
        'name': 'Middle Event',
        'description': 'Just making sure my function works'
      },
      {'name': 'Last Event', 'description': 'Last event just checking'},
    ];

    resolve(randomEvent);
  });
}

function createPlacesElement(event) {
  const tripContainer = document.getElementById('places');

  const nameElement = createEventNameElement(event.name);
  const descriptionElement = createDescription(event.description);

  tripContainer.appendChild(nameElement);
  tripContainer.appendChild(descriptionElement);
}

function createEventNameElement(text) {
  const dtElement = document.createElement('dt');
  dtElement.innerText = text;
  return dtElement;
}
function createDescription(text) {
  const ddElement = document.createElement('dd');
  ddElement.innerText = '-  ' + text;
  return ddElement;
}
