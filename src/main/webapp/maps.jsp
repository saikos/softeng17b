<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="gr.ntua.ece.softeng17b.conf.*" %>
<%@ page import="gr.ntua.ece.softeng17b.data.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Google Maps example</title>
        <link rel="stylesheet" href="./static/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="./static/font-awesome/css/font-awesome.min.css"/>
        <style>
            .gmap {
                height: 400px;
                width: 100%;
            }
        </style>
    </head>

    <body>

        <div class="container">

            <div class="row mb-5">
                <div class="col-sm">
                    <h1>A map with a marker</h1>

                    <div id="map1" class="gmap"></div>
                </div>
            </div>


            <div class="row mb-5">
                <div class="col-sm">
                    <h1>A map with multiple markers</h1>

                    <div id="map2" class="gmap"></div>
                </div>
            </div>


            <div class="row mb-5">
                <div class="col-sm">
                    <h1>A map with multiple markers from the database</h1>

                    <div id="map3" class="gmap"></div>
                </div>
            </div>

        </div>


    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
    <script src="./static/bootstrap/js/bootstrap.min.js"></script>

    <% Configuration conf = Configuration.getInstance(); %>

    <script>
        function initMaps() {
            //Map 1 (hard-coded access of configuration properties)
            var ece = {lat: <%=conf.getProperty("ece.lat")%>, lng: <%=conf.getProperty("ece.lng")%>};
            var map1 = new google.maps.Map(document.getElementById('map1'), {
                zoom: 15,
                center: ece
            });
            new google.maps.Marker({
                position: ece,
                title:"ece.ntua.gr",
                map: map1
            });

            //Map 2 (more extensible access of configuration properties)
            var locs = [];
            <%
                String commaSeparatedLocations = conf.getProperty("locations");
                String[] locations = commaSeparatedLocations.split(",");
                for (String loc : locations) {
            %>
                locs.push({
                    id : "<%=loc%>",
                    lat: <%=conf.getProperty(loc + ".lat")%>,
                    lng: <%=conf.getProperty(loc + ".lng")%>
                });
            <% } %>

            var map2 = new google.maps.Map(document.getElementById('map2'), {
                zoom: 15,
                center: locs[0]
            });

            locs.forEach(function(loc) {
                new google.maps.Marker({
                    position: loc,
                    title: loc.id,
                    map: map2
                });
            });

            //Map 3 (load the locations from the database)
            var dbLocs = [];
            <%
                List<Place> places = conf.getDataAccess().getAllPlaces();
                for (Place place : places) {
            %>
                dbLocs.push({
                    title: "<%=place.getName()%> - <%=place.getDescription()%>",
                    lat: <%=place.getLatitude()%>,
                    lng: <%=place.getLongitude()%>
                });
            <% } %>
            
            var map3 = new google.maps.Map(document.getElementById('map3'), {
                zoom: 15,
                center: dbLocs[0]
            });

            dbLocs.forEach(function(loc) {
                new google.maps.Marker({
                    position: loc,
                    title: loc.title,
                    map: map3
                });
            });
        }

    </script>

    <% String apiKey = conf.getProperty("apiKey"); %>
    <script async defer src="https://maps.googleapis.com/maps/api/js?key=<%=apiKey%>&callback=initMaps"></script>

    </body>
</html>
