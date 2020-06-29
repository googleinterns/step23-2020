
async function fetchAndSetPlace() {
  let visit = await fetchPlace();
  createVisitElement(visit);
}

function fetchPlace() {
  return new Promise((resolve, reject) => {
    let visit = {
      'name': 'Big Ben',
      'distance': 1,
      'description':
          'It is a big clock tower that is currently under contruction',
      'image': 'image here'
    };
    resolve(visit);
  });
}

function createVisitElement(visit) {
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
