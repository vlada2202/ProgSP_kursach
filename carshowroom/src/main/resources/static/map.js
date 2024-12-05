ymaps.ready(init);
function init() {
    var myMap = new ymaps.Map("map", {
        center: [53.906761, 27.561822],
        zoom: 10
    });

    var placemarks = [
        new ymaps.Placemark([53.936407, 27.593012], {
            hintContent: 'Первый офис',
            balloonContent: 'Первый офис'
        }),
        new ymaps.Placemark([53.861150, 27.489948], {
            hintContent: 'Второй офис',
            balloonContent: 'Второй офис'
        }),
        new ymaps.Placemark([53.917846, 27.589149], {
            hintContent: 'Третий офис',
            balloonContent: 'Третий офис'
        })
    ];

    placemarks.forEach(function(placemark) {
        myMap.geoObjects.add(placemark);
    });

    var placemarkData = placemarks.map(function(placemark) {
        return {
            coordinates: placemark.geometry.getCoordinates(),
            hint: placemark.properties.get('hintContent'),
            balloon: placemark.properties.get('balloonContent')
        };
    });

    console.log("Данные меток:", placemarkData);
}
