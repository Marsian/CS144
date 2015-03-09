<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.ItemInfo" %>
<html>
    <head>
        <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
        <meta charset="utf-8">
        <title>
            Item Information 
        </title>
        <style>
            html, body, #map-canvas {
                height: 100%;
                margin: 0px;
                padding: 0px
            }
        </style>
    </head>

    <body onload="initialize()" >
        <h1> Item Information </h1>
        <h2> New Search </h2>
        <form action="item" method="GET">
            itemID<br>
            <input type="text" name="id">
            <br><br>
            <input type="submit" value="Submit">
        </form>

        <h2> Results </h2>
        <h3> Location </h3>
        <div id="map-canvas" style="width:50%; height:50%"></div>
        <table border=2>
            <tr>
                <td>Entry</td><td>Value</td>
            </tr>
            <%
                ItemInfo itemInfo = (ItemInfo) request.getAttribute("itemInfo");
                //String address = itemInfo.getLocation();
                String address = itemInfo.getLocation() + " " + 
                                 itemInfo.getLongitude() + " " + 
                                 itemInfo.getLatitude();
                
                %>
                    <tr>
                        <td>ItemID</td>
                        <td><%= itemInfo.getItemId() %></td>
                    </tr>
                    <tr>
                        <td>Name</td>
                        <td><%= itemInfo.getName() %></td>
                    </tr>
                    <tr>
                        <td>SellerID</td>
                        <td><%= itemInfo.getSellerId() %></td>
                    </tr>
                    <tr>
                        <td>Buy Price</td>
                        <td>
                            <%= itemInfo.getBuyPrice() %>
                            <%
                                if( itemInfo.getBuyPrice() != "" ) {
                                    %>
                                        <button type="button" onClick="location.href='pay'">Pay Now</button>
                                    <%
                                }   
                            %>
                        </td>
                    </tr>
                    <tr>
                        <td>End Time</td>
                        <td><%= itemInfo.getEndTime() %></td>
                    </tr>
                    <tr>
                        <td>Location</td>
                        <td><%= itemInfo.getLocation() %></td>
                    </tr>
                    <tr>
                        <td>Latitude</td>
                        <td><%= itemInfo.getLatitude() %></td>
                    </tr>
                    <tr>
                        <td>Longitude</td>
                        <td><%= itemInfo.getLongitude() %></td>
                    </tr>
                    <tr>
                        <td>Description</td>
                        <td><%= itemInfo.getDescription() %></td>
                    </tr>
                <%
            %>
        </table>    
    </body>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true"></script>
    <script>
        function initialize() {
            var address = "<% out.print(address); %>"
            var geocoder = new google.maps.Geocoder();
            geocoder.geocode( { 'address': address }, function(results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                    var mapOptions = {
                        zoom: 8,
                        center: results[0].geometry.location
                    }
                    var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

                    var marker = new google.maps.Marker({ position: results[0].geometry.location,
                                                          map: map,
                                                          title: 'Hello World!'
                                                        });

                } else {
                    var mapOptions = {
                        center: new google.maps.LatLng(0,0),
                        zoom: 1
                    };
                    var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
                }
            });

        }

    </script>

</html>
