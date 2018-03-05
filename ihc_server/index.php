<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php
if (checkAuth() != "") { ?>
<html>
   <head>
      <title>I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/components/dropdown.js"></script>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
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

      <br><br>
      <div class="quicknav"><center>
        <a href="./add_event.php">
          <button class="circular ui icon button">
            <i class="upload icon"></i>
            <p>Add an Event</p>
          </button>
        </a>
        <a href="./manage_events.php">
          <button class="circular ui icon button">
            <i class="suitcase icon"></i>
            <p>Manage Events</p>
          </button>
        </a>
        <a href="./manage_resources.php">
          <button class="circular ui icon button">
            <i class="info circle icon"></i>
            <p>Manage Primary Resources</p>
          </button>
        </a>
        <a href="./add_marker.php">
          <button class="circular ui icon button">
            <i class="map pin icon"></i>
            <p>Add a Resource to the Map</p>
          </button>
        </a>
        <a href="./manage_resource_map.php">
          <button class="circular ui icon button">
            <i class="map icon"></i>
            <p>Manage Resource Map</p>
          </button>
        </a>
      <a href="./add_prize.php">
         <button class="circular ui icon button">
           <i class="plus icon"></i>
           <p>Add a Prize</p>
         </button>
      </a>
      <a href="./manage_prizes.php">
         <button class="circular ui icon button">
           <i class="edit icon"></i>
           <p>Manage Prizes</p>
         </button>
      </a>
    </center></div>
   </body>
</html>

<?php } ?>