<?php
require './admin_server/db.php';
$eventid = $_GET['eventid'];
$result = $mysqli->query("SELECT * FROM ihc_events WHERE eventid='$eventid'");
if ($result->num_rows > 0) {
   $event = $result->fetch_assoc();
}
?>

<html>
   <head>
      <title>Edit Event - I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
      <script>
      $(document).ready(function() {
         $("#pin_generator").click(function() {
            $("#pin_holder").val((Math.floor((Math.random() * 9000) + 1000)).toString());
         });
         $("#events_dropdown").hover(function() {
            $("#events_dropdown").dropdown();
         });
         $("#resources_dropdown").hover(function() {
            $("#resources_dropdown").dropdown();
         });
         $("#prizes_dropdown").hover(function() {
            $("#prizes_dropdown").dropdown();
         });
      });

      function validateForm() {
         var nameField = document.forms["eventForm"]["name"].value;
         var locationField = document.forms["eventForm"]["location"].value;
         var streetAdressField = document.forms["eventForm"]["streetaddress"].value;
         var cityField = document.forms["eventForm"]["city"].value;
         var stateField = document.forms["eventForm"]["state"].value;
         var zipField = document.forms["eventForm"]["zip"].value;
         var dateField = document.forms["eventForm"]["date"].value;
         var timeField = document.forms["eventForm"]["time"].value;
         var descriptionField = document.forms["eventForm"]["description"].value;
         var imageField = document.forms["eventForm"]["image"].value;
         if (nameField == null || nameField == "" ||
            locationField == null || locationField == "" ||
            streetAddressField == null || streetAddressField == "" ||
            cityField == null || cityField == "" ||
            stateField == null || stateField == "" ||
            zipField == null || zipField == "" ||
            dateField == null || dateField == "" ||
            timeField == null || timeField == "" ||
            descriptionField == null || descriptionField == "") {
               alert("Please fill all required fields before submitting!");
               return false;
         }
         else {
            return true;
         }
      }
      </script>
   </head>
   <body>
      <div class="siteheader">
         <br><br>
         <left class="sitenametop">I HEART CORVALLIS</left>
         <br><br>
         <left class="sitenamebottom">Administrative Suite</left>
         <br><br>
      </div>
      <ul class="navbar">
         <div class="ui pointing dropdown link item">
            <a href="./index.html" class="text">Home</a>
         </div>
         <div class="ui pointing dropdown link item" id="events_dropdown">
            <a class="text">Events</a>
            <div class="menu">
               <a href="./add_event.php" class="item">Add an Event</a>
               <a href="./manage_events.php" class="item">Manage Events</a>
            </div>
         </div>
         <div class="ui pointing dropdown link item" id="resources_dropdown">
            <a class="text">Resources</a>
            <div class="menu">
               <a class="item">Manage Primary Resources</a>
               <a class="item">Manage Resource Map</a>
            </div>
         </div>
         <div class="ui pointing dropdown link item" id="prizes_dropdown">
            <a class="text">Prizes</a>
            <div class="menu">
               <a class="item">Add a Prize</a>
               <a class="item">Manage Prizes</a>
            </div>
         </div>
      </ul>

      <div class="mainbody">
         <left class="sectionheader"><h1>Edit Event</h1></left>
         <br>
         <br><p class="requirednote">* Denotes a required field</p><br>
         <form name="eventForm" onsubmit="return validateForm()" action="./admin_server/update_events_server.php" method="post">
            <div class="elem">
               Event ID: <input class="inputbox" type="text" name="eventid" value="<?php echo $event['eventid']; ?>" readonly><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Name of Event: <input class="inputbox" type="text" name="name" value="<?php echo $event['name']; ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Name of Location: <input class="inputbox" type="text" name="location" value="<?php echo $event['location']; ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Full Address: <input class="inputbox" type="text" name="fulladdress" value="<?php echo $event['address']; ?>"><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Date: <input class="inputbox" type="date" name="date" value="<?php echo substr($event['dateandtime'], 0, 10); ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Time: <input class="inputbox" type="time" name="time" value="<?php echo substr($event['dateandtime'], 11); ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Description of Event: <input class="inputbox" type="text" name="description" value="<?php echo $event['description']; ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Cover Image: <input class="ui button" type="file" name="image" value="<?php echo $event['image']; ?>"><br><br>
            </div>
            <div class="elem">
               Link 1: <input class="inputbox" type="text" name="link1" value="<?php echo $event['link1']; ?>"><br><br>
            </div>
            <div class="elem">
               Link 2: <input class="inputbox" type="text" name="link2" value="<?php echo $event['link2']; ?>"><br><br>
            </div>
            <div class="elem">
               Link 3: <input class="inputbox" type="text" name="link3" value="<?php echo $event['link3']; ?>"><br><br>
            </div>
            <div class="elem">
               Event PIN:
               <input class="inputbox" type="text" name="pin" id="pin_holder" value="<?php echo $event['pin']; ?>">
               <button class="ui button" id="pin_generator" type="button">Generate PIN</button><br><br>
            </div>
            <input class="ui button" type="submit">
         </form>
      </div>
   </body>
</html>
