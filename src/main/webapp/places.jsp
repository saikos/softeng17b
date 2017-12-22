<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="gr.ntua.ece.softeng17b.conf.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Ajax and MVVM example</title>
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

        <div class="container" id="ko">
            <h1>
                <span data-bind="text:places().length"></span> Places
            </h1>
            <table class="table" data-bind="visible: places().length > 0">
                <thead class="thead-light">
                    <tr>
                        <th scope="col">
                            Όνομα
                        </th>
                        <th scope="col">
                            Περιγραφή
                        </th>
                        <th>         
                            Εμφάνιση στο χάρτη                   
                        </th>
                    </tr>
                </thead>  
                <tbody data-bind="foreach:places">
                    <tr>
                        <td>
                            <span data-bind="text:name"></span>
                        </td>
                        <td>
                            <span data-bind="text:description"></span>
                        </td>
                        <td>
                            <input type="checkbox" data-bind="checked: showInMap">
                        </td>
                    </tr>
                    
                </tbody>
            </table>
            <div class="gmap" id="map">
            </div>
        </div>


    <script src="https://code.jquery.com/jquery-3.2.1.min.js" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
    <script src="./static/bootstrap/js/bootstrap.min.js"></script>
    <script src="./static/knockout-3.4.2.js"></script>

    <% Configuration conf = Configuration.getInstance(); %>

    <script>
        function init() {

            var ece = {lat: <%=conf.getProperty("ece.lat")%>, lng: <%=conf.getProperty("ece.lng")%>};
            var map = new google.maps.Map(document.getElementById('map'), {
                zoom: 13,
                center: ece
            });

            console.log("Created map");

            var Place = function(name, description, lat, lng) {
                this.name = name;
                this.description = description;
                this.lat = lat;
                this.lng = lng;
                this.marker = new google.maps.Marker({
                    position: {lat: this.lat, lng: this.lng},
                    title: this.name + "\n" + description    
                });
                this.showInMap = ko.observable(false);
                this.showInMap.subscribe(function(newValue){                    
                    console.log("Changed to " + newValue);
                    if (newValue) {
                        this.marker.setMap(map);
                    }
                    else {
                        this.marker.setMap(null);
                    }                    
                }, this);
            }

            var VM = function(){
                this.places = ko.observableArray();            
            }

            VM.prototype.loadPlaces = function() {
                console.log("Loading places...");
                var opts = {
                    traditional : true,
                    cache       : false,
                    url         : "./api/places",
                    type        : "GET",
                    dataType    : "json"
                };

                return $.ajax(opts); //returns a promise
            }

            var viewModel = new VM();
            console.log("Created VM");            

            viewModel.loadPlaces().done(function(json){
                console.log("Done loading places.");
                json.results.forEach(function(placeJson){                    
                    var place = new Place(
                        placeJson.name, 
                        placeJson.description, 
                        placeJson.latitude,
                        placeJson.longitude
                    );
                    console.log(place);
                    viewModel.places.push(place);
                });
            });

            ko.applyBindings(viewModel, document.getElementById('ko'));            
            console.log("Applied bindings");
        }

    </script>

    <% String apiKey = conf.getProperty("apiKey"); %>
    <script async defer src="https://maps.googleapis.com/maps/api/js?key=<%=apiKey%>&callback=init"></script>

    </body>
</html>
