<?php
require './admin_server/db.php';
$id = $_GET['id'];
$result = $mysqli->query("SELECT * FROM ihc_resources WHERE id='$id'");
if ($result->num_rows > 0) {
   $marker = $result->fetch_assoc();
}
?>

<html>
   <head>
      <title>Edit Map Resource - I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
      <script>
      function validateForm() {
         var nameField = document.forms["markerForm"]["name"].value;
         var addressField = document.forms["markerForm"]["address"]
         var typeField = document.forms["markerForm"]["type"].value;
         if (nameField == null || nameField == "" ||
            addressField == null || addressField == "" ||
            levelField == null || levelField == "") {
               alert("Please fill all fields before submitting!");
               return false;
            }
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
         <ul>
            <div style="color: #fff; display: inline;">
               <div class="ui simple dropdown item">
                  <a href="./index.html">Home</a>
               </div>
            </div>
            <div style="color: #fff; display: inline;">
               <div class="ui simple dropdown item">
                  <a href="#">Events</a>
                  <div class="menu">
                     <div style="background-color: #dc4405;"><a href="./add_event.php">Add an Event</a></div>
                     <div style="background-color: #dc4405;"><a href="./manage_events.php">Manage Events</a></div>
                  </div>
               </div>
            </div>
            <div style="color: #fff; display: inline;">
               <div class="ui simple dropdown item">
                  <a href="#">Resources</a>
                  <div class="menu">
                     <div style="background-color: #dc4405;"><a href="./manage_primary_resources.php">Manage Resource Page</a></div>
                     <div style="background-color: #dc4405;"><a href="./manage_resource_map.php">Manage Resource Map</a></div>
                  </div>
               </div>
            </div>
            <div style="color: #fff; display: inline;">
               <div class="ui simple dropdown item">
                  <a href="#">Prizes</a>
                  <div class="menu">
                     <div style="background-color: #dc4405;"><a href="./add_prize.php">Add a Prize</a></div>
                     <div style="background-color: #dc4405;"><a href="./manage_prizes.php">Manage Prizes</a></div>
                  </div>
               </div>
            </div>
         </ul>
      </div>

      <div class="mainbody">
         <left class="sectionheader"><h1>Edit Map Resource</h1></left>
         <br>
         <br><p class="requirednote">* Denotes a required field</p><br>
         <form name="markerForm" onsubmit="return validateForm()" action="./admin_server/update_marker_server.php" method="post">
            <!--<div class="elem">
               Marker ID: <input class="inputbox" type="text" name="eventid" value="<?php /*echo $prizes['eventid']; */?>" readonly><br><br>
            </div>-->
            <div class="elem">
               <span class="requirednote">*</span>Name of Location: <input class="inputbox" type="text" name="name" value="<?php echo $marker['name']; ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Address: <input class="inputbox" type="text" name="name" value="<?php echo $marker['address']; ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Resource Type: <input class="inputbox" type="text" name="level" value="<?php echo $marker['type']; ?>"><br><br>
            </div>
            <input class="ui button" type="submit">
         </form>
      </div>
   </body>
</html>
