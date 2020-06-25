
function setTripPlaceContainerWithServlet() {
  const tripContainer = document.getElementById('Trip-Place');
  
  var randomEvent = {"name":"Random Event", "description":"This is the description"};
  var stringEvent = formatEvent(randomEvent);
  
  tripContainer.appendChild(createParagraphElement(stringEvent));
}

function formatEvent(event) {
  var formatted = '';
  formatted += event.name;
  formatted += ' Description: ' + event.description;
  return formatted + '      ';
}

function createParagraphElement(text) {
  const pElement = document.createElement('p');
  pElement.innerText = text;
  return pElement;
}