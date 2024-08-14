function loadMapScenario() {
    var map = new Microsoft.Maps.Map(document.getElementById('bing-map'), {
        credentials: 'AsbApNxLCsT0b3ESXiXJqLolw913S-mfrDHrIA88C00FFD2HCe-5H0XQzaqEUeFk',
        center: new Microsoft.Maps.Location(42.6977, 23.3219), // Sofia, Bulgaria coordinates
        mapTypeId: Microsoft.Maps.MapTypeId.road,
        zoom: 10
    });

    var pushpin = new Microsoft.Maps.Pushpin(map.getCenter(), null);
    map.entities.push(pushpin);
}