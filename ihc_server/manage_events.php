<?php
require './admin_server/db.php';
$sql = 'SELECT name, location, dateandtime FROM ihc_events';
$result = $mysqli->query("SELECT name, location, dateandtime FROM ihc_events");
$ihc_events = array();
while ($event = $result->fetch_assoc()) {
   $ihc_events[] = $event;
}
?>

<html>
   <head>
      <title>Manage Events - I Heart Corvallis Administrative Suite</title>
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
         <left class="sectionheader"><h1>Manage Events</h1></left>
         <table class="ui celled padded table">
            <thead>
               <tr>
                  <th class="single line">Name</th>
                  <th>Location</th>
                  <th>Date and Time</th>
                  <th>Action</th>
               </tr>
            </thead>
            <tbody>
               <?php foreach($ihc_events as $event): ?>
                  <tr>
                     <td><?php echo $event['name']; ?></td>
                     <td><?php echo $event['location']; ?></td>
                     <td><?php echo $event['dateandtime']; ?></td>
                     <td>
                        <a href="edit_event.php?id=<?php echo $event['eventid'] ?>" class="ui blue button">Edit</a>
                        <a onclick="return confirm('Are you sure you want to delete this entry?')" href="./admin_server/delete.php?id=<?php echo $event['eventid'] ?>" class='ui red button'>Delete</a>
                     </td>
                  </tr>
               <?php endforeach; ?>
            </tbody>
         </table>
      </div>
   </body>
</html>
