type<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

<?php
require './admin_server/db.php';
$users = $feedback = array();
$allRatings = $studentRatings = array();

$fbResult = $mysqli->query("SELECT * FROM ihc_feedback");
if ($fbResult->num_rows > 0) {
   while($comment = $fbResult->fetch_assoc()) {
      $feedback[] = $comment;
   }
}
$userResult = $mysqli->query("SELECT * FROM ihc_users");
if ($userResult->num_rows > 0) {
   while ($user = $userResult->fetch_assoc()) {
      $users[] = $user;
      $allRatings[] = $user['apprating'];
      if ($user['type'] < 2) {
         $studentRatings[] = $user['apprating'];
      }
   }
}
$avgAllRating = array_sum($allRatings) / count($allRatings);
$avgStudentRating = array_sum($studentRatings) / count($studentRatings);
?>

<?php
function getUserInfo($id) {
   $result = $mysqli->query("SELECT * FROM ihc_users WHERE id='$id'");
   if ($result->num_rows > 0) {
      $user = $result->fetch_assoc();
      return $user;
   }
}
?>

<html>
   <head>
      <title>Event Summary: <?php echo $event['name']; ?> - I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
      <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
      <script>
      google.charts.load("current", {packages:["corechart"]});
      google.charts.setOnLoadCallback(drawAllRatingsChart);
      google.charts.setOnLoadCallback(drawStudentRatingsChart);
      function drawAllRatingsChart() {
         var data = new google.visualization.arrayToDataTable([
            ['Rating', 'Users'],
            ['5', <?php echo count(array_keys($allRatings, 5)); ?>],
            ['4', <?php echo count(array_keys($allRatings, 4)); ?>],
            ['3', <?php echo count(array_keys($allRatings, 3)); ?>],
            ['2', <?php echo count(array_keys($allRatings, 2)); ?>],
            ['1', <?php echo count(array_keys($allRatings, 1)); ?>]
         ]);

         var options = {
           title: 'App Rating Spread',
           bar: {groupWidth: "80%"},
           legend: {position: "none"},
           colors: ['#0d5257'],
           vAxis: {gridlines: {count: 4}}
         };

         var chart = new google.visualization.BarChart(document.getElementById('all_ratings_columnchart'));
         chart.draw(data, options);
      }

      function drawStudentRatingsChart() {
         var data = new google.visualization.arrayToDataTable([
            ['Rating', 'Students'],
            ['5', <?php echo count(array_keys($studentRatings, 5)); ?>],
            ['4', <?php echo count(array_keys($studentRatings, 4)); ?>],
            ['3', <?php echo count(array_keys($studentRatings, 3)); ?>],
            ['2', <?php echo count(array_keys($studentRatings, 2)); ?>],
            ['1', <?php echo count(array_keys($studentRatings, 1)); ?>]
         ]);

         var options = {
           title: 'Students Only: App Rating Spread',
           bar: {groupWidth: "80%"},
           legend: {position: "none"},
           colors: ['#003b5c'],
           vAxis: {gridlines: {count: 4}}
         };

         var chart = new google.visualization.BarChart(document.getElementById('student_ratings_columnchart'));
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
         <left class="sectionheader"><h1>View App Feedback</h1></left><br>
         <div class="ui divider"></div><br>
         <div>
            <h2>App Rating</h2>
            <h4>All Users: <?php echo $avgAllRating; ?></h4>
            <h4>Students Only: <?php echo $avgStudentRating; ?></h4>
            <table>
               <tr>
                  <td><div id="all_ratings_columnchart" style="width: 50vw; height: 30vw;"></div></td>
                  <td><div id="student_ratings_columnchart" style="width: 50vw; height: 30vw;"></div></td>
               </tr>
            </table>
         </div>
         <div>
            <?php if (count($feedback) > 0) { ?>
               <h2>User Feedback</h2>
               <table class="ui celled padded table">
                  <thead>
                     <tr>
                        <th class="single line">Name</th>
                        <th>Status</th>
                        <th>Comment</th>
                     </tr>
                  </thead>
                  <tbody>
                     <?php foreach($feedback as $comment) {
                        $user = getUserInfo($comment['userid']);
                        $usertype = "";
                        if ($user['type'] == 0) $usertype = "Domestic Student";
                        else if ($user['type'] == 1) $usertype = "International Student";
                        else $usertype = "Non-Student";
                     ?>
                        <tr>
                           <td><?php echo $comment['userid']; ?></td>
                           <td><?php echo $usertype; ?></td>
                           <td><?php echo $comment['comment']; ?></td>
                        </tr>
                     <?php } ?>
                  </tbody>
               </table>
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