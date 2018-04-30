<!DOCTYPE HTML>

<?php
error_reporting(E_ALL);
require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $allTuples = $studentTuples = array();
  $sumAllRatings = $sumStudentRatings = 0;
  $avgAllRating = $avgStudentRating = 0;
  $numAllZeroes = $numStudentZeroes = 0;

  $stmt = $mysqli->prepare("SELECT U.*, F.dateandtime, F.rating, F.comment FROM ihc_users U, ihc_feedback F WHERE F.userid=U.id");
  $stmt->execute();
  $res = $stmt->get_result();
  while ($row = $res->fetch_assoc()) {
    if ($row['type'] == 0) $row['type'] = "Domestic Student";
    else if ($row['type'] == 1) $row['type'] = "International Student";
    else if ($row['type'] == 2) $row['type'] = "Faculty";
    else if ($row['type'] == 3) $row['type'] = "Resident";
    else if ($row['type'] == 4) $row['type'] = "Visitor";
    if ($row['grade'] == 0) $row['grade'] = "N/A";
    else if ($row['grade'] == 1) $row['grade'] = "Freshman";
    else if ($row['grade'] == 2) $row['grade'] = "Sophomore";
    else if ($row['grade'] == 3) $row['grade'] = "Junior";
    else if ($row['grade'] == 4) $row['grade'] = "Senior";
    else if ($row['grade'] == 5) $row['grade'] = "Graduate Student";
    else if ($row['grade'] == 6) $row['grade'] = "Doctoral Student";
    else if ($row['grade'] == 7) $row['grade'] = "Faculty";

    $allTuples[] = $row;
    $sumAllRatings += $row['rating'];
    if ($row['rating'] == 0) $numAllZeroes++;
    if ($row['type'] == "Domestic Student" || $row['type'] == "International Student" || $row['type'] == "Faculty") {
      $studentTuples[] = $row;
      $sumStudentRatings += $row['rating'];
      if ($row['rating'] == 0) $numStudentZeroes++;
    }

    $avgAllRating = $sumAllRatings/ (count($allTuples) - $numAllZeroes);
    $avgStudentRating = $sumStudentRatings/ (count($studentTuples) - $numStudentZeroes);
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
                  <th>Rating</th>
                  <th>Comment</th>
                </tr>
              </thead>
              <tbody>
                <?php foreach ($allTuples as $tuple) { ?>
                  <tr>
                    <td><?php echo $tuple['firstname'] . " " . $tuple['lastname']; ?></td>
                    <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                    <td><?php echo $tuple['type']; ?></td>
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
                  <th>Student ID #</th>
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
                    <td><?php echo $tuple['firstname'] . " " . $tuple['lastname']; ?></td>
                    <td><?php echo $tuple['studentid']; ?></td>
                    <td><?php echo $tuple['onid']; ?></td>
                    <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
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
