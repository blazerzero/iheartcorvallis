<!DOCTYPE HTML>
<html>
   <head>
      <title>Add a Resource to the Map - I Heart Corvallis Administrative Suite</title>
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
         <left class="sectionheader"><h1>Add a Resource to the Map</h1></left>
         <br><br>
         <p class="requirednote">* Denotes a required field</p><br>
         <form name="markerForm" onsubmit="return validateForm()" action="./admin_server/add_marker_server.php" method="post">
            <div class="elem">
               <span class="requirednote">*</span>
               Name of Location: <input class="inputbox" type="text" name="name"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Full Address: <textarea class="inputbox" rows="2" cols="50" name="address"></textarea><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Resource Type: <select class="ui search dropdown" name="type">
                  <option value="">Choose a resource type</option>
                  <option value="1">Activities and Entertainment</option>
                  <option value="2">Grocery Stores</option>
                  <option value="3">Restaurants</option>
                  <option value="4">Shopping</option>
                  <option value="5">City Offices</option>
                  <option value="6">OSU Campus</option>
               </select>
               <br><br>
            </div>
            <input class="ui button" type="submit">
         </form>
      </div>
   </body>
</html>
