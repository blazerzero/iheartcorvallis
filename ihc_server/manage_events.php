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
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
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
            <a class="text">Home</a>
         </div>
         <div class="ui pointing dropdown link item">
            <a class="text">Events</a>
            <div class="menu">
               <div class="item">Add an Event</div>
               <div class="item">Manage Events</div>
            </div>
         </div>
         <div class="ui pointing dropdown link item">
            <a class="text">Resources</a>
            <div class="menu">
               <div class="item">Manage Primary Resources</div>
               <div class="item">Manage Resource Map</div>
            </div>
         </div>
         <div class="ui pointing dropdown link item">
            <a class="text">Prizes</a>
            <div class="menu">
               <div class="item">Add a Prize</div>
               <div class="item">Manage a Prize</div>
               <div class="item">Manage Prize Thresholds</div>
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
