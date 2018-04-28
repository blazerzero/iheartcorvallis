<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $host = $_GET['host'];
  $events = array();
  $stmt = $mysqli->prepare("SELECT * FROM ihc_events WHERE host=?");
  $stmt->bind_param('s', $host);
  $stmt->execute();
  $eventres = $stmt->get_result();
  if ($eventres->num_rows > 0) {
    while ($event = $eventres->fetch_assoc()) {
      $events[] = $event;
    }
  }
  $attendees = $studentAttendees = array();
  $allRatings = $studentRatings = array();
  $stmt = $mysqli->prepare("SELECT CE.*, U.type FROM ihc_completed_events CE, ihc_events E, ihc_users U WHERE CE.eventid=E.eventid AND CE.userid=U.id AND E.host=?");
  $stmt->bind_param('i', $host);
  $stmt->execute();
  $completedres = $stmt->get_result();
  if ($completedres->num_rows > 0) {
    while ($listing = $completedres->fetch_assoc()) {
      $attendees[] = $listing;
      $allRatings[] = $listing['rating'];
      if ($listing['type'] < 3) {
        $studentAttendees[] = $listing;
        $studentRatings[] = $listing['rating'];
      }
    }
  }
  /*foreach ($events as $event) {
    $eventid = $event['eventid'];
    $stmt = $mysqli->prepare("SELECT * FROM ihc_completed_events WHERE eventid=?");
    $stmt->bind_param('i', $eventid);
    $stmt->execute();
    $completedres = $stmt->get_result();
    if ($completedres->num_rows > 0) {
      while ($listing = $completedres->fetch_assoc()) {
        $attendees[] = $listing;
        $allRatings[] = $listing['rating'];
      }
    }
  }*/

  $avgAllRating = array_sum($allRatings) / count($allRatings);
  $minAllRating = min($allRatings);
  $maxAllRating = max($allRatings);
  $avgStudentRating = array_sum($studentRatings) / count($studentRatings);
  $minStudentRating = min($studentRatings);
  $maxStudentRating = max($studentRatings);

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
        title: 'Overall Rating Spread',
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
        ['Rating', 'Users'],
        ['5', <?php echo count(array_keys($studentRatings, 5)); ?>],
        ['4', <?php echo count(array_keys($studentRatings, 4)); ?>],
        ['3', <?php echo count(array_keys($studentRatings, 3)); ?>],
        ['2', <?php echo count(array_keys($studentRatings, 2)); ?>],
        ['1', <?php echo count(array_keys($studentRatings, 1)); ?>]
      ]);

      var options = {
        title: 'Student/Faculty Rating Spread',
        bar: {groupWidth: "80%"},
        legend: {position: "none"},
        colors: ['#d73f09'],
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
      <left class="sectionheader"><h1>Organization Summary: <?php echo $host; ?></h1></left><br>
      <div class="ui divider"></div><br>

      <div>
        <h2>General</h2>
        <h4>Number of Events: <?php echo count($events); ?></h4>
        <h4>Total Number of Attendees: <?php echo count($attendees); ?></h4>
        <h4>Number of Student/Faculty Attendees: <?php echo count($studentAttendees); ?></h4>
        <?php if (count($attendees) > 0) { ?>
          <h4>Average Rating: <?php echo $avgAllRating; ?></h4>
          <h4>Student/Faculty Rating: <?php echo $avgStudentRating; ?></h4>
          <table>
            <tr>
              <td><div id="all_ratings_columnchart" style="width: 50vw; height: 30vw;"></div></td>
              <td><div id="student_ratings_columnchart" style="width: 50vw; height: 30vw;"></div></td>
            </tr>
          </table><br>

          <div class="ui divider"></div><br>
          <div>
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

    <?php
    $stmt->close();
    $mysqli->close();
  }
  else {
    $url = "./admin_auth.php";
    echo "<script type='text/javascript'>document.location.href = '$url';</script>";
  }
  ?>
