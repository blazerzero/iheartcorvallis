<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

<html>
   <head>
      <title>Add a User - I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
      <script>
      function validateForm() {
         var nameField = document.forms["userForm"]["email"].value;
         var levelField = document.forms["userForm"]["password"].value;
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
                  <a style="color: red;" href="./admin_server/logout.php">Logout</a>
               </div>
            </div>
         </ul>
      </div>

      <div class="mainbody">
         <left class="sectionheader"><h1>Add a User</h1></left>
         <br><br>
         <p class="requirednote">* Denotes a required field</p><br>
         <form name="prizeForm" onsubmit="return validateForm()" action="./admin_server/add_users_server.php" method="post">
            <div class="elem">
               <span class="requirednote">*</span>
               Email: <input class="inputbox" type="email" name="email"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>
               Password: <input class="inputbox" type="text" name="password"><br><br>
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