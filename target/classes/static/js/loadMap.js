function initMap() {
    const myLatLng = {lat: 10.800474, lng: 106.655813};
    const map = new google.maps.Map(document.getElementById("map"), {
        center: myLatLng,
        zoom: 18,
    });
    new google.maps.Marker({
        position: myLatLng,
        map,
        title: "My current location",
    });
}