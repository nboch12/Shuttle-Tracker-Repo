<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Display GPS</title>
		<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
		<link href='https://fonts.googleapis.com/css?family=Lato:700' rel='stylesheet' type='text/css'>
		<link href='css/display.css' rel='stylesheet' type='text/css'>
	
		
		
		
		<script
		src="http://maps.googleapis.com/maps/api/js">
		</script>
		
		<script>					
			var shuttles = new Array();
			var markers = new Array();
			var stopMarkers = new Array();
			var map;	
			
			// Shuttle Icon
			var icon = {
					url: "https://maps.google.com/mapfiles/kml/shapes/rec_bus.png",
					scaledSize: new google.maps.Size(30, 30),
			}
			
			function initialize()
			{			
				var stops = new Array();
				var infowindow = new Array();
				<% String[] shuttleStops = (String[])request.getAttribute("shuttleStops"); %>
				
				map = new google.maps.Map(document.getElementById("googleMap"),{
					center:new google.maps.LatLng(39.946397, -76.734519),
					  zoom:16,
					  mapTypeId:google.maps.MapTypeId.ROADMAP
				});
				
				// Create blank array of shuttle locations
				for( var i=0; i<10; i++)
				{
					markers[i] = new google.maps.Marker({
			 			 position: new google.maps.LatLng(0.0,0.0),				  
			  			 icon: icon,
			  			 map: map
			  			});
				}
				
								
				var flagIcon = {
						url: "http://moena.us/flag2.png",
						scaledSize: new google.maps.Size(20, 30),
				}
				
								
				// Get shuttle stop location from servlet
				<c:set var="count" value="0" scope="page" />
				<c:forEach items="${shuttleStops}" var="info">
					stops[${count}] = "${info}";
					<c:set var="count" value="${count + 1}" scope="page"/>
				</c:forEach>
				
				for( var i=0; i<${count}; i++)
				{
					var parseStop = stops[i].split(",");
										
					stopMarkers[i] = new google.maps.Marker({
						map: map,
						position: new google.maps.LatLng(parseStop[1],parseStop[2]),						
						clickable: true,
						icon: flagIcon
					});
					
					stopMarkers[i].info = new google.maps.InfoWindow({
					    content: '<h2>'+parseStop[0]+'</h2>Estimated Arrival: <br>',
					    position: new google.maps.LatLng(parseFloat(parseStop[1])+0.0005,parseStop[2]),
					    minWidth: 400,
					    maxWidth: 400
					 });
					
					google.maps.event.addListener(stopMarkers[i],'click', function() {
						var marker_map = this.getMap();
					    this.info.open(marker_map);
					});					
				}
				
				
				
					
			}	
			
			function getCoordinates()
			{
				var locations = [];
				var shuttleUpdate;
				var xhr = new XMLHttpRequest();
				
				
				xhr.open('GET', '/shuttletracker/ajax/updateData', true);

				// Load data from AJAX response into array
				xhr.responseType = 'text';

				xhr.onload = function () {
				    if (xhr.readyState === xhr.DONE) {
				        if (xhr.status === 200) {
				        	shuttleUpdate = xhr.responseText;
				        	console.log("Response: " + JSON.stringify(shuttleUpdate));
				        	locations = shuttleUpdate.split(",");
				        	addMarkers(locations);
				        }
				    }
				};
				
				xhr.send(null);	
			}
			
			
			function addMarkers(locations)
			{	
				// Iterate through array and set locations for the map
				if( locations.length > 1 ) 
				{
					for( var i=0; i<locations.length/2; i++ )
					{	
						// Location array is formatted as location[i] = "lat,lon" and needs to be parsed
						var temp1 = locations[2*i];
						locations[2*i] = temp1;
						
						var temp2 = locations[(2*i)+1];
						locations[(2*i)+1] = temp2;
						
						// Assign lat/lon to Google Maps LatLng object 
						shuttles[i] = new google.maps.LatLng(temp1,temp2);						
						
						// Update position
						markers[i].setPosition(shuttles[i]);
					}
				}
				
				// Sets refresh rate
				setTimeout(getCoordinates, 1000);
			}
			
			google.maps.event.addDomListener(window, 'load', initialize);
			setTimeout(getCoordinates, 1000);
			
	</script>
	</head>
	<body>
	<div class="bg">
		<div class="mainDiv">
			<div class="header">
				Shuttle Location System
			</div>
		
			<div id="googleMap" style="width:100%;height:650px; border-bottom: solid black 1px;"></div>
			
			<div class="bodyText">
				
				Stop &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<div class="dropdown">
				
				<button class="dropbtn">Northside Commons</button>
					<div class="dropdown-content">
						<a href="#">Northside Commons</a>
						<a href="#">Wolf Hall to Creek</a>
						<a href="#">Wolf Hall to West</a>
						<a href="#">Creek Crossing</a>
						<a href="#">Spring Garden Apartments</a>
						<a href="#">Readco Lot</a>
						<a href="#">Rail Trail Lot</a>
						<a href="#">Grumbacher/Diehl Lot</a>
					</div>				
				</div>
				
				<br><br>Arrival
					
			</div>
	
	  		
	  		<div>${result}</div>
		</div>
	</div>
	</body>
</html>