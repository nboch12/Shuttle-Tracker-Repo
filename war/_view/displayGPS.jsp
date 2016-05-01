<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Display GPS</title>
		<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
		<link href='https://fonts.googleapis.com/css?family=Lato:700' rel='stylesheet' type='text/css'>
		<link href='css/display.css' rel='stylesheet' type='text/css'>
	
		
		<% String locations = (String)request.getAttribute("locations"); %>
		
		<script
		src="http://maps.googleapis.com/maps/api/js">
		</script>
		
		<script>		
			var locations2 = [<%=locations%>];
			var locations = [];
			var prevLocations = [];
			var shuttles = new Array();
			var markers = new Array();
			var map;			
			
			// Set coordinates for shuttle stops
			var myCenter=new google.maps.LatLng(39.946397, -76.734519);
			var wolfStop=new google.maps.LatLng(39.9455,-76.7304);
			var creekStop=new google.maps.LatLng(39.9477,-76.7278);
			var northSideStop=new google.maps.LatLng(39.9496,-76.7337);
			var grumStop=new google.maps.LatLng(39.9455,-76.7352);
			var railTrailStop=new google.maps.LatLng(39.9475,-76.7370);
			var readCoStop=new google.maps.LatLng(39.9446,-76.7397);
			var springGardenStop=new google.maps.LatLng(39.9429,-76.7383);			
			
			// Shuttle Icon
			var icon = {
					url: "https://maps.google.com/mapfiles/kml/shapes/rec_bus.png",
					scaledSize: new google.maps.Size(30, 30),
			}
			
			
			// AJAX Object used to request/receive data
			var ajaxObj = { 
				    options: {
				        url: "/shuttletracker/ajax/updateData", //The resource that delivers loc data.
				        type: 'GET',
				        dataType: "text"
				    },
				    delay: 250, //(milliseconds) the interval between successive gets.
				    errorCount: 0, //running total of ajax errors.
				    errorThreshold: 5, //the number of ajax errors beyond which the get cycle should cease.
				    ticker: null, //setTimeout reference - allows the get cycle to be cancelled with clearTimeout(ajaxObj.ticker);
				    get: function () { //a function which initiates 
				        if (ajaxObj.errorCount < ajaxObj.errorThreshold) {
				            ajaxObj.ticker = setTimeout(getMarkerData, ajaxObj.delay);
				        }
				    },
				    fail: function (jqXHR, textStatus, errorThrown) {
				        console.log(errorThrown);
				        ajaxObj.errorCount++;
				    }
				};
			
			//Ajax master routine
			function getMarkerData() {
				//console.log("Getting Marker Data");
			    $.ajax(ajaxObj.options)
			        .done(addMarkers) 	//fires when ajax returns successfully
			    .fail(ajaxObj.fail) 	//fires when an ajax error occurs
			    .always(ajaxObj.get); 	//fires after ajax success or ajax error
			}	
			
			function initialize()
			{				
				map = new google.maps.Map(document.getElementById("googleMap"),{
					center:myCenter,
					  zoom:16,
					  mapTypeId:google.maps.MapTypeId.ROADMAP
				});
				
				var flagIcon = {
						url: "http://moena.us/flag2.png",
						scaledSize: new google.maps.Size(20, 30),
				}
				
				// Create blank array of shuttle locations
				for( var i=0; i<10; i++)
				{
					markers[i] = new google.maps.Marker({
			 			 position: new google.maps.LatLng(0.0,0.0),				  
			  			 icon: icon,
			  			 map: map
			  			});
				}		
								
				var WolfMarker=new google.maps.Marker({
					  position:wolfStop,
					  icon: flagIcon,
					  });
					WolfMarker.setMap(map);
					
				var CreekMarker=new google.maps.Marker({
					position:creekStop,
					icon: flagIcon,
					  });
					CreekMarker.setMap(map);
					
				var NorthSideMarker=new google.maps.Marker({
					position:northSideStop,
					icon: flagIcon,
					  });
					NorthSideMarker.setMap(map);
					
				var GrumMarker=new google.maps.Marker({
					position:grumStop,
					icon: flagIcon,
					 });
					GrumMarker.setMap(map);	
					
				var RailMarker=new google.maps.Marker({
					position:railTrailStop,
					icon: flagIcon,
					 });
					RailMarker.setMap(map);	
					
				var ReadCoMarker=new google.maps.Marker({
					position:readCoStop,
					icon: flagIcon,
					 });
					ReadCoMarker.setMap(map);	
					
				var SpringMarker=new google.maps.Marker({
					position:springGardenStop,
					icon: flagIcon,
					 });
					SpringMarker.setMap(map);
			}			
			
			
			function addMarkers()
			{	
				var xhr = new XMLHttpRequest();
				var shuttleUpdate;
				xhr.open('GET', '/shuttletracker/ajax/updateData', true);

				// Load data from AJAX response into array
				xhr.responseType = 'text';

				xhr.onload = function () {
				    if (xhr.readyState === xhr.DONE) {
				        if (xhr.status === 200) {
				        	shuttleUpdate = xhr.responseText;
				        	//console.log("Response: " + shuttleUpdate);
				        	locations = shuttleUpdate.split(",");
				        }
				    }
				};

				xhr.send(null);				
				
				// Iterate through array and set locations for the map
				if( locations.length > 1 ) 
				{
					for( var i=0; i<locations.length/2; i++ )
					{	
						// Location array is formatted as location[i] = "lat,lon" and needs to be parsed
						var temp1 = locations[2*i];
						temp1 = temp1.substring(1,temp1.length);
						locations[2*i] = temp1;
						
						var temp2 = locations[(2*i)+1];
						temp2 = temp2.substring(0,temp2.length-2);
						temp2 = temp2.replace("\"", "0");
						locations[(2*i)+1] = temp2;
						
						
						// Assign lat/lon to Google Maps LatLng object 
						shuttles[i] = new google.maps.LatLng(temp1,temp2);
						
						// Only update if the position changes
						if( locations[2*i] != prevLocations[2*i] || locations[(2*i)+1] != prevLocations[(2*i)+1]) {
							console.log("Shuttle: " + (i+1) + " changed");
							markers[i].setPosition(shuttles[i]);
							
							prevLocations[2*i] = locations[2*i];
							prevLocations[2*i+1] = locations[2*i+1];
						}
					}
				}
			}
			
			google.maps.event.addDomListener(window, 'load', initialize);
			addMarkers();
			ajaxObj.get();
			
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