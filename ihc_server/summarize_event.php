<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $eventid = $_GET['eventid'];
  $stmt = $mysqli->prepare("SELECT * FROM ihc_events WHERE eventid=?");
  $stmt->bind_param('i', $eventid);
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {
    $event = $result->fetch_assoc();
  }

  $stmt = $mysqli->prepare("SELECT * FROM ihc_completed_events WHERE eventid=?");
  $stmt->bind_param('i', $eventid);
  $stmt->execute();
  $result = $stmt->get_result();
  $numAttendees = $result->num_rows;

  /* Variable Declarations */
  $numFreshmen = $numSophomores = $numJuniors = $numSeniors = $numGrad = $numDoc = $numFaculty = 0;
  $numDomStudents = $numIntlStudents = $numNonStudents = 0;
  $ages = $allAttendees = $students = $allRatings = $studentRatings = $comments = $studentComments = array();
  $minAge = $maxAge = $minAllRating = $maxAllRating = $minStudentRating = $maxStudentRating = 0;
  $avgAllRating = $avgStudentRating = 0;

  /* Get Tuples from Completed Events for This Event
  and Get the Information of the Users Who Attended This Event */
  while ($row = $result->fetch_assoc()) {
    $userid = $row['userid'];
    $stmt = $mysqli->prepare("SELECT * FROM ihc_users WHERE id=?");
    $stmt->bind_param('i', $userid);
    $stmt->execute();
    $res = $stmt->get_result();
    if ($res->num_rows > 0) {
      $user = $res->fetch_assoc();
      $name = $user['firstname'] . " " . $user['lastname'];
      $allAttendees[] = $user;
      $grade = (int)$user['grade'];
      $today = date("Y-m-d h:i:s");
      $birthdate = date("Y-m-d h:i:s", strtotime($user['birthdate']));
      $userAge = $today - $birthdate;
      $ages[] = $userAge;
      $allRatings[] = $row['rating'];

      $usertype = (int)$user['type'];
      $typeString = $gradeString = "";
      if ($usertype == 0) $typeString = "Domestic Student";
      else if ($usertype == 1) $typeString = "International Student";
      else if ($usertype == 2) $typeString = "Faculty";
      else if ($usertype == 3) $typeString = "Resident";
      else if ($usertype == 4) $typeString = "Visitor";
      if ($grade == 0) $gradeString = "N/A";
      else if ($grade == 1) $gradeString = "Freshman";
      else if ($grade == 2) $gradeString = "Sophomore";
      else if ($grade == 3) $gradeString = "Junior";
      else if ($grade == 4) $gradeString = "Senior";
      else if ($grade == 5) $gradeString = "Graduate Student";
      else if ($grade == 6) $gradeString = "Doctoral Student";
      else if ($grade == 7) $gradeString = "Faculty";

      if (strlen($row['comment']) > 0 || (int)$row['rating'] != 0) {
        //$comments[] = $row['comment'];
        $comments[] = array("userid" => $user['id'], "dateandtime" => $row['dateandtime'], "name" => $name, "type" => $typeString, "rating" => $row['rating'], "comment" => $row['comment']);
      }

      if ($grade == 1) { $numFreshmen++; }
      else if ($grade == 2) { $numSophomores++; }
      else if ($grade == 3) { $numJuniors++; }
      else if ($grade == 4) { $numSeniors++; }
      else if ($grade == 5) { $numGrad++; }
      else if ($grade == 6) { $numDoc++; }

      if ($usertype < 3) {
        $students[] = $user;
        $studentRatings[] = $row['rating'];
        if (strlen($row['comment']) > 0 || (int)$user['rating'] != 0) {
          $studentComments[] = array("userid" => $user['id'], "dateandtime" => $row['dateandtime'], "name" => $name, "studentid" => $user['studentid'], "onid" => $user['onid'], "grade" => $gradeString, "type" => $typeString, "rating" => $row['rating'], "comment" => $row['comment']);
        }
      }
      if ($usertype == 0) { $numDomStudents++; }
      else if ($usertype == 1) { $numIntlStudents++; }
      else if ($usertype == 2) { $numFaculty++; }
      else if ($usertype >= 3) { $numNonStudents++; }
    }
  }
  $numStudents = $numDomStudents + $numIntlStudents;
  $minAge = min($ages);
  $maxAge = max($ages);
  $minAllRating = min($allRatings);
  $maxAllRating = max($allRatings);
  $minStudentRating = min($studentRatings);
  $maxStudentRating = max($studentRatings);
  $avgAllRating = array_sum($allRatings) / count($allRatings);
  $avgStudentRating = array_sum($studentRatings) / count($studentRatings);
  ?>

  <html>
  <head>
    <title>Event Summary: <?php echo $event['name']; ?> - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
    google.charts.load("current", {packages:["corechart"]});
    google.charts.setOnLoadCallback(drawAttendeeChart);
    google.charts.setOnLoadCallback(drawStudentAttendeeChart);
    google.charts.setOnLoadCallback(drawStudentStatusChart);
    google.charts.setOnLoadCallback(drawStudentAgeChart);
    google.charts.setOnLoadCallback(drawAllRatingsChart);
    google.charts.setOnLoadCallback(drawStudentRatingsChart);

    function drawAttendeeChart() {
      var data = google.visualization.arrayToDataTable([
        ['Attendee Type', 'Number of Attendees of This Type'],
        ['Students and Faculty', <?php echo $numStudents + $numFaculty; ?>],
        ['Non-Students', <?php echo $numNonStudents; ?>]
      ]);

      var options = {
        title: 'Attendees by Type',
        pieHole: 0.4,
      };

      var chart = new google.visualization.PieChart(document.getElementById('all_attendees_donutchart'));
      chart.draw(data, options);
    }

    function drawStudentAttendeeChart() {
      var data = google.visualization.arrayToDataTable([
        ['Student Attendee Type', 'Number of Attendees of This Type'],
        ['Domestic Students', <?php echo $numDomStudents; ?>],
        ['International Students', <?php echo $numIntlStudents; ?>],
        ['Faculty', <?php echo $numFaculty; ?>]
      ]);

      var options = {
        title: 'Student Attendees by Student Type',
        pieHole: 0.4,
      };

      var chart = new google.visualization.PieChart(document.getElementById('student_attendees_donutchart'));
      chart.draw(data, options);
    }

    function drawStudentStatusChart() {
      var data = google.visualization.arrayToDataTable([
        ['Status', 'Number of Student Attendees of That Status'],
        ['Freshmen', <?php echo $numFreshmen; ?>],
        ['Sophomores', <?php echo $numSophomores; ?>],
        ['Juniors', <?php echo $numJuniors; ?>],
        ['Seniors', <?php echo $numSeniors; ?>],
        ['Graduate Students', <?php echo $numGrad; ?>],
        ['Doctoral Students', <?php echo $numDoc; ?>],
        ['Faculty', <?php echo $numFaculty; ?>]
      ]);

      var options = {
        title: 'Student Attendees by Status',
        pieHole: 0.4,
      };

      var chart = new google.visualization.PieChart(document.getElementById('student_status_donutchart'));
      chart.draw(data, options);
    }

    function drawStudentAgeChart() {
      var data = new google.visualization.DataTable();
      data.addColumn('string', 'Age');
      data.addColumn('number', 'Users');
      <?php foreach (range($minAge, $maxAge) as $i): ?>
      data.addRows([
        ['<?php echo $i; ?>', <?php echo count(array_keys($ages, $i)); ?>]
      ]);
      <?php endforeach; ?>

      var options = {
        title: 'Attendee Age Spread',
        bar: {groupWidth: "80%"},
        legend: {position: "none"},
        colors: ['#d73f09'],
        hAxis: {title: 'Age'},
        vAxis: {title: 'Number of Users', gridlines: {count: 5}}
      };

      var chart = new google.visualization.ColumnChart(document.getElementById('student_ages_columnchart'));
      chart.draw(data, options);
    }

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
        title: 'Overall Event Rating Spread',
        bar: {groupWidth: "80%"},
        legend: {position: "none"},
        colors: ['#0d5257'],
        hAxis: {title: 'Number of Users', gridlines: {count: 5}},
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
        title: 'Student Event Rating Spread',
        bar: {groupWidth: "80%"},
        legend: {position: "none"},
        colors: ['#003b5c'],
        hAxis: {title: 'Number of Students', gridlines: {count: 5}},
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
      <left class="sectionheader"><h1>Event Summary: <?php echo $event['name']; ?></h1></left><br>
      <div class="ui divider"></div><br>

      <div>
        <h2>Event Information</h2>
        <h4>Location: <?php echo $event['location']; ?></h4>
        <h4>Date and Time:
          <?php
          if ($event['startdt'] == '1900-01-01 00:00:00' && $event['enddt'] == '2099-12-31 23:59:59') {
            echo 'Anytime';
          }
          else {
            $startdt = date('M d, Y g:i A', strtotime($event['startdt']));
            $enddt = date('M d, Y g:i A', strtotime($event['enddt']));
            echo $startdt . " - " . $enddt;
          }
          ?>
        </h4>
      </div><br>
      <div class="ui divider"></div><br>

      <div>
        <h2>General Statistics</h2>
        <h4>Number of Attendees: <?php echo $numAttendees; ?></h4>
        <?php if ($numAttendees > 0) { ?>

          <!-- ALL EVENT AND USER STATS GO IN HERE -->
          <h4>Average Overall Rating: <?php echo $avgAllRating; ?></h4>
          <h4>Average Student Rating: <?php echo $avgStudentRating; ?></h4>
          <table>
            <tr>
              <td><div id="all_attendees_donutchart" style="width: 50vw; height: 30vw;"></div></td>
              <td>
                <h4>All Attendees</h4>
                <?php foreach($allAttendees as $attendee) { ?>
                  <span><?php echo $attendee['firstname'] . " " . $attendee['lastname'] . "<br>"; ?></span>
                <?php } ?>
              </td>
            </tr>
            <tr>
              <td><div id="student_attendees_donutchart" style="width: 50vw; height: 30vw;"></div></td>
              <td>
                <h4>Student Attendees</h4>
                <?php foreach($students as $student) { ?>
                  <span><?php echo $student['firstname'] . " " . $student['lastname'] . "<br>"; ?></span>
                <?php } ?>
              </td>
            </tr>
            <tr>
              <td><div id="student_status_donutchart" style="width: 50vw; height: 30vw;"></div></td>
            </tr>
          </table>
          <div id="student_ages_columnchart" style="width: 40vw; height: 30em;"></div>
          <table>
            <tr>
              <td><div id="all_ratings_columnchart" style="width: 40vw; height: 30vw;"></div></td>
              <td><div id="student_ratings_columnchart" style="width: 40vw; height: 30vw;"></div></td>
            </tr>
          </table>

          <!-- EVENT FEEDBACK -->
          <?php if (count($comments) > 0) { ?>
            <div class="ui divider"></div><br>

            <div>
              <h2>Event Feedback: All Feedback</h2>
              <table class="ui celled padded table">
                <thead>
                  <tr>
                    <th class="single line">Name</th>
                    <th>Date and Time</th>
                    <th>User Type</th>
                    <th>Event Rating</th>
                    <th>Comment</th>
                  </tr>
                </thead>
                <tbody>
                  <?php foreach($comments as $tuple) { ?>
                    <tr>
                      <td><?php echo $tuple['name']; ?></td>
                      <td><?php echo $tuple['dateandtime']; ?></td>
                      <td><?php echo $tuple['type']; ?></td>
                      <td><?php if ($tuple['rating'] != 0) echo $tuple['rating']; ?></td>
                      <td><?php echo $tuple['comment']; ?></td>
                    </tr>
                  <?php } ?>
                </tbody>
              </table>
            </div><br>
          <?php } ?>

          <?php if (count($studentComments) > 0) { ?>
            <div class="ui divider"></div><br>

            <div>
              <h2>Event Feedback: Student/Faculty Feedback</h2>
              <table class="ui celled padded table">
                <thead>
                  <tr>
                    <th class="single line">Name</th>
                    <th>Student ID #</th>
                    <th>ONID Username</th>
                    <th>Date and Time</th>
                    <th>User Type</th>
                    <th>Class Standing</th>
                    <th>Event Rating</th>
                    <th>Comment</th>
                  </tr>
                </thead>
                <tbody>
                  <?php foreach($studentComments as $tuple) { ?>
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
          <?php } ?>

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
