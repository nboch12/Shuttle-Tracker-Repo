<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Display GPS</title>
		
		<link href='https://fonts.googleapis.com/css?family=Lato:700' rel='stylesheet' type='text/css'>
		
		<style>
			.mainDiv
			{
				width: 90%;
				height: 900px;
				margin: auto;
				border: 2px solid black;
				border-radius: 10px;
				box-shadow: 0 5px 10px 0 rgba(0, 0, 0, 0.2), 0 10px 20px 0 rgba(0, 0, 0, 0.19);
				z-index: 1;
				background-color: white;
				margin-top: 10px;
				
			}
			.header
			{
				background-color: #008752;
				color: #EFEFEF;
				padding-top: 20px;
				padding-left: 40px;
				padding-bottom: 15px;
				border-top-left-radius: 8px;
				border-top-right-radius: 8px;
				font-family: 'Lato', sans-serif;
				font-weight: 700;
				font-size: 45px;
				text-shadow: 2px 2px #2F2F2F;			
			}
			.arrivalTime
			{
				min-width: 600px;
				height: 50px;
				background-color: white;
				padding: 30px 180px 30px 180pxs;
			}
			.bodyText
			{
				font-family: 'Lato', sans-serif;
				font-weight: 400;
				font-size: 40px;
				width:100%;
				padding-top:50px;	
				padding-left: 300px;		
			}
			.dropbtn
			{
				background-color: #efefef;
				width: 600px;
				height: 50px;
				padding: 30px 180px 30px 180pxs;
				font-family: 'Lato', sans-serif;
				font-weight: 400;
				font-size: 25px;		
				border-radius: 6px;	
			}
			
			/* The container <div> - needed to position the dropdown content */
			.dropdown
			{
				position: relative;
				display: inline-block;
			}
			
			/* Dropdown Content (Hidden by Default) */
			.dropdown-content
			{
				display: none;
				
				position: absolute;
				background-color: #f9f9f9;
				min-width: 600px;
				box-shadow: 0px 4px 8px 0px rgba(0,0,0,.2);
				font-size: 20px;
				
			}
			
			/* Links inside the dropdown */
			.dropdown-content a 
			{
				color: black;
				padding 25px 25px 25px; 25px;
				text-decoration: none;
				display: block;
				height: 30px;
			}
			
			/* Change color of dropdown links on hover */
			.dropdown-content a:hover {background-color: #f1f1f1}
			
			/* Show the dropdown menu on hover */
			.dropdown:hover .dropdown-content 
			{
			    display: block;
			}
			
			/* Change the background color of the dropdown button when the dropdown content is shown */
			.dropdown:hover .dropbtn 
			{
			    background-color: #e0e0e0;
			}
			
			.bg
			{
				 background-size: 100%;
				 top:0;
				 left:0;
			    position: fixed;
				opacity: 1;
				height:100%;
			    width: 100%;
				z-index: 16;
				overflow: scroll;
				
			}
			.bg:after
			{
				background: url('http://moena.us/overlay3.png') center repeat;
				background-color: rgb(180, 180, 180);
				content: "";
			    position: fixed;
				bottom: 0;
				height:10000px;
				max-width: 10000px;
				left: 0;
				right: 0;
				opacity: .3;
				z-index: -1;
			}
			
			.error 
			{
				color: red;
			}
					
			td.label {
				text-align: right;
			}
		
		</style>
		
		<% String locations = (String)request.getAttribute("locations"); %>
		
		<script
		src="http://maps.googleapis.com/maps/api/js">
		</script>
		
		<script>
		/*
			window.setInterval(function(){
				onRefresh();
				}, 10);*/
			
		
			var locations = [<%=locations%>];
			var shuttles = new Array();
			var markers = new Array();
			
			// Shuttle Icon
			var icon = {
					url: "https://maps.google.com/mapfiles/kml/shapes/rec_bus.png",
					scaledSize: new google.maps.Size(30, 30),
			}
			
			for( var i=1; i<=locations.length; i++ )
			{	
				// Parse lat/lon 
				var latlon = locations[i-1].split(",");
				
				// Assign lat/lon to Google Maps LatLng object 
				shuttles[i-1] = new google.maps.LatLng(latlon[0],latlon[1]);
				
				// Assign shuttle location to Google Maps Marker
				markers[i-1] =new google.maps.Marker({
					 			 position: shuttles[i-1],				  
					  			 icon: icon
					  			});
				
				console.log("Location: " + i + " Lat: "+ latlon[0] + " Lon: " + latlon[1]);
			}
			
			
			// Set coordinates for shuttle stops
			var myCenter=new google.maps.LatLng(39.9455,-76.7321);
			var wolfStop=new google.maps.LatLng(39.9455,-76.7304);
			var creekStop=new google.maps.LatLng(39.9477,-76.7278);
			var northSideStop=new google.maps.LatLng(39.9496,-76.7337);
			var grumStop=new google.maps.LatLng(39.9455,-76.7352);
			var railTrailStop=new google.maps.LatLng(39.9475,-76.7370);
			var readCoStop=new google.maps.LatLng(39.9446,-76.7397);
			var springGardenStop=new google.maps.LatLng(39.9429,-76.7383);
			
			var mapProp = {
					  center:myCenter,
					  zoom:15,
					  mapTypeId:google.maps.MapTypeId.ROADMAP
					  };
			
			
			function initialize()
			{				
				var map=new google.maps.Map(document.getElementById("googleMap"),mapProp);
				
				for( var i=0; i<markers.length; i++ )
				{
					markers[i].setMap(map);
				}
				
				var flagIcon = {
						url: "http://moena.us/flag2.png",
						scaledSize: new google.maps.Size(20, 30),
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
			
			google.maps.event.addDomListener(window, 'load', initialize);
				
			
			var num=0.00001
			
			function onRefresh()
			{
				console.log("onRefresh");
				num=num+.000002;
			}
			
			
			
	</script>
	</head>
	<body>
	<div class="bg">
		<div class="mainDiv">
			<div class="header">
				Shuttle Location System
			</div>
		
			<div id="googleMap" style="width:100%;height:440px; border-bottom: solid black 1px;"></div>
			
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
				<input type="hidden" value="Test" onclick="onRefresh();">
					
			</div>
			</div>
	
			<form action="${pageContext.servletContext.contextPath}/displayGPS" method="post">
	  			<input type="Submit" name="updateData" value="" size="0">
	  		</form>
	  		
	  		<div>${result}</div>
		</div>
	</div>
	</body>
</html>