<?php
require './admin_server/db.php';
$result = $mysqli->query("SELECT * FROM ihc_prizes");
$ihc_prizes = array();
$temp = array();
while ($prize = $result->fetch_assoc()) {
   $ihc_prizes[] = $prize;
}

$indexLeft = 0;
$pointer = 0;
$i = 0;
$indexRight = 1;

// INSERTION SORT TO SORT PRIZES IN DESCENDING ORDER FROM GOLD TO BRONZE
for ($i = 0; $i < count($ihc_prizes)-1; $i++) {
   while ($indexLeft >= 0
         && (($ihc_prizes[$indexLeft]["level"] == "silver" && $ihc_prizes[$indexRight]["level"] == "gold")
         || ($ihc_prizes[$indexLeft]["level"] == "bronze" && $ihc_prizes[$indexRight]["level"] == "gold")
         || ($ihc_prizes[$indexLeft]["level"] == "bronze" && $ihc_prizes[$indexRight]["level"] == "silver"))) {
            $temp = $ihc_prizes[$indexLeft];
            $ihc_prizes[$indexLeft] = $ihc_prizes[$indexRight];
            $ihc_prizes[$indexRight] = $temp;
            $indexLeft--;
            $indexRight--;
   }
   $pointer++;
   $indexLeft = $pointer;
   $indexRight = $pointer + 1;
}
?>

<html>
   <head>
      <title>Manage Prizes - I Heart Corvallis Administrative Suite</title>
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
         <left class="sectionheader"><h1>Manage Prizes</h1></left>
         <table class="ui celled padded table">
            <thead>
               <tr>
                  <th class="single line">Name</th>
                  <th>Level</th>
                  <th>Action</th>
               </tr>
            </thead>
            <tbody>
               <?php foreach($ihc_prizes as $prize): ?>
                  <tr>
                     <td><?php echo $prize['name']; ?></td>
                     <td><?php echo $prize['level']; ?></td>
                     <td>
                        <a href="edit_prize.php?eventid=<?php echo $prize['prizeid'] ?>" class="ui blue button">Edit</a>
                        <a onclick="return confirm('Are you sure you want to delete this prize?')" href="./admin_server/delete_prize.php?prizeid=<?php echo $prize['prizeid'] ?>" class='ui red button'>Delete</a>
                     </td>
                  </tr>
               <?php endforeach; ?>
            </tbody>
         </table>
      </div>
   </body>
</html>
