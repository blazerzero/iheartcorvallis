<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

<?php
require './admin_server/db.php';
$host = $_GET['host'];
$events = array();
$eventres = $mysqli->query("SELECT * FROM ihc_events WHERE host='$host'");
if ($eventres->num_rows > 0) {
   while ($event = $eventres->fetch_assoc()) {
      $events[] = $event;
   }
}
$attendees = array();
$allRatings = array();
foreach ($events as $event) {
   $eventid = $event['eventid'];
   $completedres = $mysqli->query("SELECT * FROM ihc_completed_events WHERE eventid='$eventid'");
   if ($completedres->num_rows > 0) {
      while ($listing = $completedres->fetch_assoc()) {
         $attendees[] = $listing;
         $allRatings[] = $listing['rating'];
      }
   }
}

$avgAllRating = array_sum($allRatings) / count($allRatings);
$minAllRating = min($allRatings);
$maxAllRating = max($allRatings);

?>

<?php
function getOverallRating($eventid) {
   $ratingsArr = array();
   $result = $mysqli->query("SELECT * FROM ihc_completed_events WHERE eventid='$eventid'");
   while ($row = $result->fetch_assoc()) {
      $ratingsArr[] = $row['rating'];
   }
   $rating = array_sum($ratingsArr) / count($ratingsArr);
   return $rating;
}

function getStudentRating($eventid) {
   $ratingsArr = array();
   $ratings = 0;
   $result = $mysqli->query("SELECT * FROM ihc_completed_events WHERE eventid='$eventid'");
   while ($row = $result->fetch_assoc()) {
      $userid = $row['userid'];
      $res = $mysqli->query("SELECT * FROM ihc_users WHERE id='$userid'");
      if ($res->num_rows > 0) {
         $user = $res->fetch_assoc();
         $studenttype = (int)$user['studenttype'];
         if ($studenttype < 2) {
            $ratingsArr[] = $row['rating'];
         }
      }
   }
   if (count($ratingsArr) > 0) $rating = array_sum($ratingsArr) / count($ratingsArr);
   else $rating = "-";
   return $rating;
}
?>

<html>
   <head>
      <title>Organization Summary: <?php echo $host; ?> - I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
      <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
      <script type="text/javascript">
      google.charts.load("current", {packages:["corechart"]});
      google.charts.setOnLoadCallback(drawAllRatingsChart);

      function drawAllRatingsChart() {
         var data = new google.visualization.arrayToDataTable([
            ['Rating', 'Users'],
            ['1', <?php echo count(array_keys($allRatings, 1)); ?>],
            ['2', <?php echo count(array_keys($allRatings, 2)); ?>],
            ['3', <?php echo count(array_keys($allRatings, 3)); ?>],
            ['4', <?php echo count(array_keys($allRatings, 4)); ?>],
            ['5', <?php echo count(array_keys($allRatings, 5)); ?>]
         ]);

         var options = {
           title: 'Overall Rating Spread',
           bar: {groupWidth: "80%"},
           legend: {position: "none"},
           colors: ['#0d5257'],
           vAxis: {gridlines: {count: 4}}
         };

         var chart = new google.visualization.ColumnChart(document.getElementById('all_ratings_columnchart'));
         chart.draw(data, options);
      }
      </script>

      <script>
      $(document).ready(function() {
         $("#siteheader").load("siteheader.html");
      });
      </script>
   </head>
   <body>
      <div class="siteheader" id="siteheader"></div>

      <div class="mainbody">
         <left class="sectionheader"><h1>Organization Summary: <?php echo $host; ?></h1></left><br>
         <div class="ui divider"></div><br>

         <div>
            <h2>General</h2>
            <h4>Number of Events: <?php echo count($events); ?></h4>
            <h4>Total Number of Attendees: <?php echo count($attendees); ?></h4>
            <?php if (count($attendees) > 0) { ?>
               <h4>Average Rating: <?php echo $avgAllRating; ?></h4>
               <div id="all_ratings_columnchart" style="width: 50vw; height: 30vw;"></div></td><br>
               <h2>Event Summaries</h2>
               <table class="ui celled padded table">
                  <thead>
                     <tr>
                        <th class="single line">Name</th>
                        <th>Location</th>
                        <th>Date and Time</th>
                        <th>Summary</th>
                     </tr>
                  </thead>
                  <tbody>
                     <?php foreach ($events as $event) { ?>
                        <tr>
                           <th class="single line">Name</th>
                           <th>Location</th>
                           <th>Date and Time</th>
                           <th>Summary</th>
                        </tr>
                     </thead>
                     <tbody>
                        <?php foreach ($events as $event) { ?>
                           <tr>
                              <td><?php echo $event['name'] ?></td>
                              <td><?php echo $event['location']; ?></td>
                              <td><?php echo $event['startdt'] . " - " . $event['enddt']; ?></td>
                              <td><a href="summarize_event.php?eventid=<?php echo $event['eventid'] ?>" class="ui green button">View</a></td>
                           </tr>
                        <?php } ?>
                     </tbody>
                  </table>
               </div>
            <?php } ?>
         </div>
   </body>
</html>

<?php }
else {
   $url = "./admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>
