<!DOCTYPE HTML>
<html>
   <head>
      <title>Add a Prize - I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
      <script>
      $(document).ready(function() {
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
         <left class="sectionheader"><h1>Add a Prize</h1></left>
         <br><br>
         <form name="prizeForm" onsubmit="return validateForm()" action="./admin_server/add_prizes_server.php" method="post">
            <div class="elem">
               Name of Prize: <input class="inputbox" type="text" name="name"><br><br>
            </div>
            <div class="elem">
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