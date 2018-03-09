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

$result = $mysqli->query("SELECT * FROM ihc_completed_events WHERE eventid='$eventid'");
$numAttendees = $result->num_rows;
$numFreshmen = $numSophomores = $numJuniors = $numSeniors = $numGrad = 0;
while ($row = $result->fetch_assoc()) {
   $userid = $row['userid'];
   $res = $mysqli->query("SELECT grade FROM ihc_users WHERE id='$userid'");
   if ($res->num_rows > 0) {
      $user = $res->fetch_assoc();
      $grade = (int)$user['grade'];
      if ($grade == 1) {
         $numFreshmen++;
      }
      else if ($grade == 2) {
         $numSophomores++;
      }
      else if ($grade == 3) {
         $numJuniors++;
      }
      else if ($grade == 4) {
         $numSeniors++;
      }
      else if ($grade == 5) {
         $numGrad++;
      }
   }
}
?>

<html>
   <head>
      <title>Summarize Event - I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
      <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
      <script type="text/javascript">
         google.charts.load("current", {packages:["corechart"]});
         google.charts.setOnLoadCallback(drawChart);
         function drawChart() {
            var numFresh = <?php echo $numFreshmen; ?>;
            var numSoph = <?php echo $numSophomores; ?>;
            var numJr = <?php echo $numJuniors; ?>;
            var numSr = <?php echo $numSeniors; ?>;
            var numG = <?php echo $numGrad; ?>;

            var data = google.visualization.arrayToDataTable([
               ['Grade', 'Number of Attendees of that Grade'],
               ['Freshmen', numFresh],
               ['Sophomores', numSoph],
               ['Juniors', numJr],
               ['Seniors', numSr],
               ['Graduate Students', numG]
            ]);

            var options = {
               title: 'Student Statuses',
               pieHole: 0.4,
            };

            var chart = new google.visualization.PieChart(document.getElementById('attendees_donutchart'));
            chart.draw(data, options);
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
         <left class="sectionheader"><h1>Summarize Event</h1></left><br><br>
         <div>
            <h2>Event Information</h2>
            <h4>Name: <?php echo $event['name']; ?></h4>
            <h4>Location: <?php echo $event['location']; ?></h4>
            <h4>Date and Time: <?php echo $event['dateandtime']; ?></h4>
         </div>
         <br><br>
         <div>
            <h2>General Statistics</h2>
            <h4>Number of Attendees: <?php echo $numAttendees; ?></h4>
            <?php if ($numAttendees > 0) { ?>
               <!-- ALL EVENT AND USER STATS GO IN HERE -->
               <div id="attendees_donutchart" style="width: 900px; height: 500px;"></div>
            <?php } ?>
         </div>

      </div>
   </body>
</html>

<?php }
else {
   $url = "./admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>
