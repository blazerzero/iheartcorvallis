<?php
require './admin_server/db.php';
$prizeid = $_GET['prizeid'];
$result = $mysqli->query("SELECT * FROM ihc_events WHERE prizeid='$prizeid'");
if ($result->num_rows > 0) {
   $prize = $result->fetch_assoc();
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
         var nameField = document.forms["prizeForm"]["name"].value;
         var levelField = document.forms["prizeForm"]["level"].value;
         if (nameField == null || nameField == "" ||
            levelField == null || levelField == "") {
               alert("Please fill both fields before submitting!");
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
         <form name="prizeForm" onsubmit="return validateForm()" action="./admin_server/update_prizes_server.php" method="post">
            <div class="elem">
               Prize ID: <input class="inputbox" type="text" name="eventid" value="<?php echo $prizes['eventid']; ?>" readonly><br><br>
            </div>
            <div class="elem">
               Name of Prize: <input class="inputbox" type="text" name="name" value="<?php echo $prize['name']; ?>"><br><br>
            </div>
            <div class="elem">
               Prize Level: <input class="inputbox" type="text" name="level" value="<?php echo $prize['level']; ?>"><br><br>
            </div>
            <input class="ui button" type="submit">
         </form>
      </div>
   </body>
</html>
