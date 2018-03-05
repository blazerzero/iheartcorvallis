<?php
require './admin_server/db.php';
$id = $_GET['id'];
$result = $mysqli->query("SELECT * FROM ihc_resource_info WHERE id='$id'");
if ($result->num_rows > 0) {
   $resource = $result->fetch_assoc();
}
?>

<!DOCTYPE HTML>
<html>
   <head>
      <title>Edit Resource - I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
      <script>
      function validateForm() {
         var titleField = document.forms["resourceForm"]["title"].value;
         var descriptionField = document.forms["resourceForm"]["description"].value;
         if (titleField == null || titleField == "" ||
            descriptionField == null || descriptionField == "") {
               alert("Please fill all required fields before submitting!");
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
         </ul>
      </div>

      <div class="mainbody">
         <left class="sectionheader"><h1>Edit Resource</h1></left>
         <br><br>
         <p class="requirednote">* Denotes a required field</p><br>
         <form name="resourceForm" onsubmit="return validateForm()" action="./admin_server/update_primary_resource_server.php" method="post">
            <div class="elem" style="display: none;">
               Resource ID: <input class="inputbox" type="text" name="title" value="<?php echo $resource['id']; ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Resource Title: <input class="inputbox" type="text" name="title" value="<?php echo $resource['title']; ?>"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Description: <textarea class="inputbox" rows="4" cols="50" name="description"><?php echo $resource['description']; ?></textarea><br><br>
            </div>
            <div class="elem">
               Link: <input class="inputbox" type="text" name="link" value="<?php echo $resource['link']; ?>"><br><br>
            </div>
            <input class="ui button" type="submit">
         </form>
      </div>
   </body>
</html>
