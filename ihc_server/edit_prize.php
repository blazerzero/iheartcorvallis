<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

<?php
require './admin_server/db.php';
$prizeid = $_GET['prizeid'];
$result = $mysqli->query("SELECT * FROM ihc_prizes WHERE prizeid='$prizeid'");
if ($result->num_rows > 0) {
   $prize = $result->fetch_assoc();
}
?>

<html>
   <head>
      <title>Edit Prize - I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
      <script>
      $(document).ready(function() {
         $("#pin_generator").click(function() {
            $("#pin_holder").val((Math.floor((Math.random() * 9000) + 1000)).toString());
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
         <ul>
            <div style="color: #fff; display: inline;">
               <div class="ui simple dropdown item">
                  <a href="./index.php">Home</a>
               </div>
            </div>
            <div style="color: #fff; display: inline;">
               <div class="ui simple dropdown item">
                  <a href="#">Events</a>
                  <div class="menu">
                     <div style="background-color: #d73f09;"><a href="./add_event.php">Add an Event</a></div>
                     <div style="background-color: #d73f09;"><a href="./manage_events.php">Manage Events</a></div>
                  </div>
               </div>
            </div>
            <div style="color: #fff; display: inline;">
               <div class="ui simple dropdown item">
                  <a href="#">Resources</a>
                  <div class="menu">
                     <div style="background-color: #d73f09;"><a href="./add_primary_resource.php">Add to Resource Page</a></div>
                     <div style="background-color: #d73f09;"><a href="./manage_primary_resources.php">Manage Resource Page</a></div>
                     <div style="background-color: #d73f09;"><a href="./add_marker.php">Add Resource to Map </a></div>
                     <div style="background-color: #d73f09;"><a href="./manage_resource_map.php">Manage Resource Map</a></div>
                  </div>
               </div>
            </div>
            <div style="color: #fff; display: inline;">
               <div class="ui simple dropdown item">
                  <a href="#">Prizes</a>
                  <div class="menu">
                     <div style="background-color: #d73f09;"><a href="./add_prize.php">Add a Prize</a></div>
                     <div style="background-color: #d73f09;"><a href="./manage_prizes.php">Manage Prizes</a></div>
                  </div>
               </div>
            </div>
            <div style="color: #fff; display: inline;">
               <div class="ui simple dropdown item">
                  <a href="./manage_about.php">About Page</a>
               </div>
            </div>
            <div style="color: #fff; display: inline;">
               <div class="ui simple dropdown item">
                  <a style="color: red;" href="./admin_server/logout.php">Logout</a>
               </div>
            </div>
         </ul>
      </div>

      <div class="mainbody">
         <left class="sectionheader"><h1>Edit Prize</h1></left>
         <br>
         <br><p class="requirednote">* Denotes a required field</p><br>
         <form name="prizeForm" onsubmit="return validateForm()" action="./admin_server/update_prizes_server.php" method="post">
            <div class="elem" style="display: none">
               Prize ID: <input class="inputbox" type="text" name="prizeid" value="<?php echo $prize['prizeid']; ?>" readonly><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Name of Prize: <input class="inputbox" type="text" name="name" value="<?php echo $prize['name']; ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Prize Level: <select class="ui search dropdown" name="level">
                  <option value="">Choose a level</option>
                  <option value="1">Gold</option>
                  <option value="2">Silver</option>
                  <option value="3">Bronze</option>
               </select>
               <br><br>
            </div>
            <input class="ui button" type="submit">
         </form>
      </div>
   </body>
</html>

<?php }
else {
   $url = "./admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>
