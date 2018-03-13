<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

<?php 
function generatePIN() {
   $i = 0;
   $pin = "";
   while ($i < 4) {
      $pin .= mt_rand(0, 9);
      $i++;
   }
   return $pin;
}
?>

<html>
   <head>
      <title>Add an Event - I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
      <script>
      /*$(document).ready(function() {
         $("#pin_generator").click(function() {
            alert("Making random pin");
            $("#pin_holder").val((Math.floor((Math.random() * 9000) + 1000)).toString());
         });
      });*/

      /*function generatePIN() {
         alert("PIN!");
         document.forms["eventForm"]["pin"].value = Math.floor((Math.random() * 9000) + 1000).toString();
      }*/

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
         var pinField = document.forms["eventForm"]["pin"].value;
         if (nameField == null || nameField == "" ||
            locationField == null || locationField == "" ||
            streetAddressField == null || streetAddressField == "" ||
            cityField == null || cityField == "" ||
            stateField == null || stateField == "" ||
            zipField == null || zipField == "" ||
            dateField == null || dateField == "" ||
            timeField == null || timeField == "" ||
            descriptionField == null || descriptionField == ""
            pinField == null || pinField = "") {
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
                     <div style="background-color: #dc4405;"><a href="./add_event.php">Add an Event</a></div>
                     <div style="background-color: #dc4405;"><a href="./manage_events.php">Manage Events</a></div>
                  </div>
               </div>
            </div>
            <div style="color: #fff; display: inline;">
               <div class="ui simple dropdown item">
                  <a href="#">Resources</a>
                  <div class="menu">
                     <div style="background-color: #dc4405;"><a href="./add_primary_resource.php">Add to Resource Page</a></div>
                     <div style="background-color: #dc4405;"><a href="./manage_primary_resources.php">Manage Resource Page</a></div>
                     <div style="background-color: #dc4405;"><a href="./add_marker.php">Add Resource to Map </a></div>
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
         <left class="sectionheader"><h1>Add an Event</h1></left>
         <br><br>
         <p class="requirednote">* Denotes a required field</p><br>
         <form name="eventForm" onsubmit="return validateForm()" action="./admin_server/add_events_server.php" method="post">
            <div class="elem">
               <span class="requirednote">*</span>
               Name of Event: <input class="inputbox" type="text" name="name"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Name of Location: <input class="inputbox" type="text" name="location"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Full Address: <textarea class="inputbox" rows="2" cols="50" name="fulladdress"></textarea><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Date: <input class="inputbox" type="date" name="date"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Time: <input class="inputbox" type="time" name="time"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Description of Event: <textarea class="inputbox" rows="3" cols="50" name="description"></textarea><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Cover Image: <input class="ui button" type="file" name="image"><br><br>
            </div>
            <div class="elem">
               Link 1: <input class="inputbox" type="text" name="link1"><br><br>
            </div>
            <div class="elem">
               Link 2: <input class="inputbox" type="text" name="link2"><br><br>
            </div>
            <div class="elem">
               Link 3: <input class="inputbox"type="text" name="link3"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Event PIN:
               <input class="inputbox" type="text" name="pin" id="pin_holder" value="<?php echo generatePIN(); ?>" readonly><br><br>
               <!--<button class="ui button" id="pin_generator" type="button">Generate PIN</button><br><br>-->
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
