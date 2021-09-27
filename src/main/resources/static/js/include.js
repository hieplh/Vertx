function includeHTML(id, file) {
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        document.getElementById(id).innerHTML = this.responseText;
    };
//    xhttp.open("GET", "http://cvonline.ddns.net/" + file, true);
    xhttp.open("GET", "http://localhost:8080/" + file, true);
    xhttp.send();
}