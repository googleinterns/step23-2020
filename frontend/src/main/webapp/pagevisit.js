function pageData() {
  var visit = {
    'name': 'Big Ben',
    'distance': 1,
    'description':
        'It is a big clock tower that is currently under contruction',
    'image': 'image here'
  };

  createVisitElement(visit);
}

function createVisitElement(visist) {
  const nameContainer = document.getElementById('name');

  nameContainer.appendChild(createNameElement(visit));
  const descriptionContainer = document.getElementById('description');
  descriptionContainer.innerText = visit.description;
  const imageContainer = document.getElementById('image');
  imageContainer.innerText = visit.image;
}

function createNameElement(place) {
  const nameElement = document.createElement('span');
  nameElement.innerText = place.name + ' Distance: ' + place.distance + 'mi';
  return nameElement;
}