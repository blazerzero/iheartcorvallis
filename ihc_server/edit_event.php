<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

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

      function validateForm() {
         var nameField = document.forms["eventForm"]["name"].value;
         var hostField = document.forms["eventForm"]["host"].value;
         var locationField = document.forms["eventForm"]["location"].value;
         var streetAdressField = document.forms["eventForm"]["streetaddress"].value;
         var cityField = document.forms["eventForm"]["city"].value;
         var stateField = document.forms["eventForm"]["state"].value;
         var zipField = document.forms["eventForm"]["zip"].value;
         var startDateField = document.forms["eventForm"]["startdate"].value;
         var startTimeField = document.forms["eventForm"]["starttime"].value;
         var endDateField = document.forms["eventForm"]["enddate"].value;
         var endTimeField = document.forms["eventForm"]["endtime"].value;
         var descriptionField = document.forms["eventForm"]["description"].value;
         var imageField = document.forms["eventForm"]["image"].value;
         var pinField = document.forms["eventForm"]["pin"].value;
         if (nameField == null || nameField == "" ||
            hostField == null || hostField == "" ||
            locationField == null || locationField == "" ||
            streetAddressField == null || streetAddressField == "" ||
            cityField == null || cityField == "" ||
            stateField == null || stateField == "" ||
            zipField == null || zipField == "" ||
            startDateField == null || startDateField == "" ||
            startTimeField == null || startTimeField == "" ||
            endDateField == null || endDateField == "" ||
            endTimeField == null || endTimeField == "" ||
            descriptionField == null || descriptionField == "" ||
            imageField == null || imageField == "" ||
            pinField == null || pinField = "") {
               alert("Please fill all required fields before submitting!");
               return false;
         }
         else {
            return true;
         }
      }
      </script>
      <script>
      $(document).ready(function() {
         $("#siteheader").load("siteheader.html");
      });
      </script>
   </head>
   <body>
      <div class="siteheader" id="siteheader"></div>

      <div class="mainbody">
         <left class="sectionheader"><h1>Edit Event</h1></left><br>
         <div class="ui divider"></div><br>

         <p class="requirednote">* Denotes a required field</p><br>
         <form name="eventForm" onsubmit="return validateForm()" action="./admin_server/update_events_server.php" method="post">
            <div class="elem" style="display: none">
               Event ID: <input class="inputbox" type="text" name="eventid" value="<?php echo $event['eventid']; ?>" readonly><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Event Name: <input class="inputbox" type="text" name="name" value="<?php echo $event['name']; ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Event Host: <input class="inputbox" type="text" name="host" value="<?php echo $event['host']; ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Event Location: <input class="inputbox" type="text" name="location" value="<?php echo $event['location']; ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Full Address: <textarea class="inputbox" rows="2" cols="50" name="fulladdress"><?php echo $event['address']; ?></textarea><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Start Date: <input class="inputbox" type="date" name="startdate" value="<?php echo substr($event['startdt'], 0, 10); ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Start Time: <input class="inputbox" type="time" name="starttime" value="<?php echo substr($event['startdt'], 11); ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               End Date: <input class="inputbox" type="date" name="enddate" value="<?php echo substr($event['enddt'], 0, 10); ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               End Time: <input class="inputbox" type="time" name="endtime" value="<?php echo substr($event['enddt'], 11); ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Description of Event: <textarea class="inputbox" type="text" name="description" rows="4" cols="50"><?php echo $event['description']; ?></textarea><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Cover Image: <input class="ui button" type="file" name="image" value="<?php echo $event['image']; ?>"><br><br>
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
