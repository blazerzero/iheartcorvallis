<!DOCTYPE HTML>

<?php
error_reporting(E_ALL);
require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $allTuples = $studentTuples = array();
  $sumAllRating = $sumStudentRating = 0;
  $avgAllRating = $avgStudentRating = 0;
  $numAllZeroes = $numStudentZeroes = 0;

  $stmt = $mysqli->prepare("SELECT * FROM ihc_feedback");
  $stmt->execute();
  $fbResult = $stmt->get_result();
  if ($fbResult->num_rows > 0) {
    while ($fbRow = $fbResult->fetch_assoc()) {
      $userid = $fbRow['userid'];
      $dateandtime = $fbRow['dateandtime'];
      $rating = $fbRow['rating'];
      $sumAllRating += $rating;
      if ($rating == 0) $numAllZeroes++;
      $comment = $fbRow['comment'];

      $stmt = $mysqli->prepare("SELECT * FROM ihc_users WHERE id=?");
      $stmt->bind_param('i', $userid);
      $stmt->execute();
      $userRes = $stmt->get_result();
      if ($userRes->num_rows > 0) {
        $userRow = $userRes->fetch_assoc();
        $name = $userRow['firstname'] . " " . $userRow['lastname'];
        $type = "";
        $grade = "";
        if ($userRow['type'] == 0) $type = "Domestic Student";
        else if ($userRow['type'] == 1) $type = "International Student";
        else if ($userRow['type'] == 2) $type = "Faculty";
        else if ($userRow['type'] == 3) $type = "Resident";
        else if ($userRow['type'] == 4) $type = "Visitor";
        if ($userRow['grade'] == 0) $grade = "N/A";
        else if ($userRow['grade'] == 1) $grade = "Freshman";
        else if ($userRow['grade'] == 2) $grade = "Sophomore";
        else if ($userRow['grade'] == 3) $grade = "Junior";
        else if ($userRow['grade'] == 4) $grade = "Senior";
        else if ($userRow['grade'] == 5) $grade = "Graduate Student";
        else if ($userRow['grade'] == 6) $grade = "Doctoral Student";
        else if ($userRow['grade'] == 7) $grade = "Faculty";

        $allTuples[] = array("userid" => $userid, "dateandtime" => $dateandtime, "name" => $name, "type" => $type, "grade" => $grade, "rating" => $rating, "comment" => $comment);
        if ($userRow['type'] < 2) {
          $sumStudentRating += $rating;
          if ($rating == 0) $numStudentZeroes++;
          $studentid = $userRow['studentid'];
          $onid = $userRow['onid'];
          $studentTuples[] = array("userid" => $userid, "dateandtime" => $dateandtime, "name" => $name, "studentid" => $studentid, "onid" => $onid, "type" => $type, "grade" => $grade, "rating" => $rating, "comment" => $comment);
        }
      }
    }

    $avgAllRating = $sumAllRating / (count($allTuples) - $numAllZeroes);
    $avgStudentRating = $sumStudentRating / (count($studentTuples) - $numStudentZeroes);
    $allRatingCounts = array_count_values(array_column($allTuples, 'rating'));
    $studentRatingCounts = array_count_values(array_column($studentTuples, 'rating'));
    $allRatings = array_column($allTuples, 'rating');
    for ($i = 1; $i <= 5; $i++) {
      if (!array_key_exists("$i", $allRatingCounts)) $allRatingCounts["$i"] = 0;
      if (!array_key_exists("$i", $studentRatingCounts)) $studentRatingCounts["$i"] = 0;
    }
  }
  ?>

  <html>
  <head>
    <title>View App Feedback - I Heart Corvallis Administrative Suite</title>
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
      var numFive = <?php echo $allRatingCounts['5']; ?>;
      var numFour = <?php echo $allRatingCounts['4']; ?>;
      var numThree = <?php echo $allRatingCounts['3']; ?>;
      var numTwo = <?php echo $allRatingCounts['2']; ?>;
      var numOne = <?php echo $allRatingCounts['1']; ?>;
      var data = new google.visualization.arrayToDataTable([
        ['Rating', 'Users'],
        ['5', numFive],
        ['4', numFour],
        ['3', numThree],
        ['2', numTwo],
        ['1', numOne]
      ]);

      var options = {
        title: 'App Rating Spread',
        bar: {groupWidth: "80%"},
        legend: {position: "none"},
        colors: ['#0d5257'],
        vAxis: {gridlines: {count: 2}}
      };

      var chart = new google.visualization.BarChart(document.getElementById('all_ratings_columnchart'));
      chart.draw(data, options);
    }

    function drawStudentRatingsChart() {
      var numFive = <?php echo $studentRatingCounts['5']; ?>;
      var numFour = <?php echo $studentRatingCounts['4']; ?>;
      var numThree = <?php echo $studentRatingCounts['3']; ?>;
      var numTwo = <?php echo $studentRatingCounts['2']; ?>;
      var numOne = <?php echo $studentRatingCounts['1']; ?>;
      var data = new google.visualization.arrayToDataTable([
        ['Rating', 'Students and Faculty'],
        ['5', numFive],
        ['4', numFour],
        ['3', numThree],
        ['2', numTwo],
        ['1', numOne]
      ]);

      var options = {
        title: 'Students and Faculty Only: App Rating Spread',
        bar: {groupWidth: "80%"},
        legend: {position: "none"},
        colors: ['#003b5c'],
        vAxis: {gridlines: {count: 2}}
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
      <left class="sectionheader"><h1>View App Feedback</h1></left></br>
      <div class="ui divider"></div><br>

      <div>
        <h2>App Rating</h2>
        <h4>All Users: <?php echo $avgAllRating; ?></h4>
        <h4>Total Number of Ratings: <?php echo (count($allTuples) - $numAllZeroes); ?></h4><br>
        <h4>Students and Faculty Only: <?php echo $avgStudentRating; ?></h4>
        <h4>Number of Student and Faculty Ratings: <?php echo (count($studentTuples) - $numStudentZeroes); ?></h4>

        <?php if (count($allTuples) > 0) { ?>

          <!-- APP RATING DISTRIBUTION -->
          <table>
            <tr>
              <td><div id="all_ratings_columnchart" style="width: 40vw; height: 30vw;"></div></td>
              <td><div id="student_ratings_columnchart" style="width: 40vw; height: 30vw;"></div></td>
            </tr>
          </table><br>

          <div class="ui divider"></div><br>

          <div>
            <h2>Feedback: All Users</h2>
            <table class="ui celled padded table">
              <thead>
                <tr>
                  <th class="single line">Name</th>
                  <th>Date and Time</th>
                  <th>User Type</th>
                  <th>Class Standing</th>
                  <th>Rating</th>
                  <th>Comment</th>
                </tr>
              </thead>
              <tbody>
                <?php foreach ($allTuples as $tuple) { ?>
                  <tr>
                    <td><?php echo $tuple['name']; ?></td>
                    <td><?php echo $tuple['dateandtime']; ?></td>
                    <td><?php echo $tuple['type']; ?></td>
                    <td><?php echo $tuple['grade']; ?></td>
                    <td><?php if ($tuple['rating'] != 0) echo $tuple['rating']; ?></td>
                    <td><?php echo $tuple['comment']; ?></td>
                  </tr>
                <?php } ?>
              </tbody>
            </table>
          </div><br>

          <div class="ui divider"></div><br>

          <div>
            <h2>Feedback: Students and Faculty</h2>
            <table class="ui celled padded table">
              <thead>
                <tr>
                  <th class="single line">Name</th>
                  <th>Student Id #</th>
                  <th>ONID Username</th>
                  <th>Date and Time</th>
                  <th>User Type</th>
                  <th>Class Standing</th>
                  <th>Rating</th>
                  <th>Comment</th>
                </tr>
              </thead>
              <tbody>
                <?php foreach ($studentTuples as $tuple) { ?>
                  <tr>
                    <td><?php echo $tuple['name']; ?></td>
                    <td><?php echo $tuple['studentid']; ?></td>
                    <td><?php echo $tuple['onid']; ?></td>
                    <td><?php echo $tuple['dateandtime']; ?></td>
                    <td><?php echo $tuple['type']; ?></td>
                    <td><?php echo $tuple['grade']; ?></td>
                    <td><?php if ($tuple['rating'] != 0) echo $tuple['rating']; ?></td>
                    <td><?php echo $tuple['comment']; ?></td>
                  </tr>
                <?php } ?>
              </tbody>
            </table>
          </div>
        <?php } else { ?>
          <h4>No feedback has been received yet.</h4>
        <?php } ?>
      </div>
    </div>
  </body>
  </html>

  <?php
  $stmt->close();
  $mysqli->close();
}
else {
  $url = "./admin_auth.php";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>
