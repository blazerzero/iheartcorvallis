<!DOCTYPE HTML>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $id = $_GET['id'];
  $allTuples = $studentTuples = $nonStudentTuples = array();
  $question = $choicesStr = "";
  $choices = array();

  $stmt = $mysqli->prepare("SELECT * FROM ihc_survey WHERE id=?");
  $stmt->bind_param('i', $id);
  $stmt->execute();
  $questionres = $stmt->get_result();

  if ($questionres->num_rows > 0) {
    $questionrow = $questionres->fetch_assoc();
    $question = $questionrow['question'];
    $choicesStr = $questionrow['choices'];
    $token = strtok($choicesStr, ",");
    while ($token !== false) {
      $choices[] = $token;
      $token = strtok(",");
    }
  }

  $stmt = $mysqli->prepare("SELECT U.*, R.dateandtime, R.response FROM ihc_users U, ihc_survey_responses R WHERE R.userid=U.id AND questionid=?");
  $stmt->bind_param('i', $id);
  $stmt->execute();
  $res = $stmt->get_result();
  if ($res->num_rows > 0) {
    while ($row = $res->fetch_assoc()) {
      $dateandtime = $row['dateandtime'];
      $response = $row['response'];
      $name = $row['firstname'] . " " . $row['lastname'];

      $types = array('Domestic Student', 'International Student', 'Faculty/Staff', 'Resident', 'Visitor');
      $grades = array('N/A', 'Freshman', 'Sophomore', 'Junior', 'Senior', 'Graduate Student', 'Doctoral Student', 'Faculty/Staff');
      $type = $types[$row['type']];
      $grade = $grades[$row['grade']];

      $allTuples[] = array("userid" => $userid, "dateandtime" => $dateandtime, "name" => $name, "type" => $type, "response" => $response);
      if ($row['type'] < 3) {
        $studentid = $row['studentid'];
        $onid = $row['onid'];
        $studentTuples[] = array("userid" => $userid, "dateandtime" => $dateandtime, "name" => $name, "studentid" => $studentid, "onid" => $onid, "grade" => $grade, "type" => $type, "response" => $response);
      }
      else {
        $nonStudentTuples[] = array("userid" => $userid, "dateandtime" => $dateandtime, "name" => $name, "type" => $type, "response" => $response);
      }
    }
  }

  ?>

  <html>
  <head>
    <title>View Question Responses: <?php echo $event['name']; ?> - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script>
    $(document).ready(function() {
      $("#siteheader").load("siteheader.html");
      $("#generalbtn").click(function() {
        document.getElementById("general").scrollIntoView();
      });
      $("#allresponses").click(function() {
        document.getElementById("all_reponsediv").scrollIntoView();
      });
      $("#studentresponses").click(function() {
        document.getElementById("student_responsediv").scrollIntoView();
      });
      $("#nonstudentresponses").click(function() {
        document.getElementById("nonstudent_responsediv").scrollIntoView();
      });
    });
    </script>
  </head>
  <body>
    <div class="siteheader" id="siteheader"></div>

    <div class="mainbody" id="mainbody">
      <left class="sectionheader"><h1>View Question Responses</h1></left><br>
      <div class="ui divider"></div><br>

      <div id="general">
        <h2>General</h2>
        <h4>Question: <?php echo $question; ?></h4>
        <h4>Choices:
          <?php
          for ($i = 0; $i < count($choices) - 1; $i++) echo $choices[$i]. ", ";
          echo $choices[count($choices) - 1];
          ?>
        </h4>
        <h4>Number of Responses: <?php echo count($allTuples); ?></h4>
        <h4>Number of OSU-Affiliated Responses: <?php echo count($studentTuples); ?></h4>
        <h4>Number of Resident/Visitor Responses: <?php echo count($nonStudentTuples); ?></h4>
      </div><br>
      <div class="quicknav">
        <button class="ui orange button ihc" id="generalbtn">General</button>
        <button class="ui orange button ihc" id="allresponses">All Responses</button>
        <button class="ui orange button ihc" id="studentresponses">OSU-Affiliated Responses</button>
        <button class="ui orange button ihc" id="nonstudentresponses">Resident/Visitor Responses</button>
      </div>

      <div class="ui divider"></div><br>

      <div id="all_reponsediv">
        <h2>All Responses</h2>
        <table class ="ui celled padded table" style="width: 30vw; height: 50vw; display: block; overflow-y:auto; overflow-x:auto">
          <thead>
            <tr>
              <th class="single line">Name</th>
              <th>Date and Time</th>
              <th>User Type</th>
              <th>Response</th>
            </tr>
          </thead>
          <tbody>
            <?php foreach ($allTuples as $tuple) { ?>
              <tr>
                <td><?php echo $tuple['name']; ?></td>
                <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                <td><?php echo $tuple['type']; ?></td>
                <td><?php echo $tuple['response']; ?></td>
              </tr>
            <?php } ?>
          </tbody>
        </table>
      </div><br>

      <div class="ui divider"></div><br>

      <div id="student_responsediv">
        <h2>OSU-Affiliated Responses</h2>
        <table class ="ui celled padded table" style="width: 50vw; height: 50vw; display: block; overflow-y:auto; overflow-x:auto">
          <thead>
            <tr>
              <th class="single line">Name</th>
              <th>Student ID #</th>
              <th>ONID Username</th>
              <th>Date and Time</th>
              <th>User Type</th>
              <th>Class Standing</th>
              <th>Response</th>
            </tr>
          </thead>
          <tbody>
            <?php foreach ($studentTuples as $tuple) { ?>
              <tr>
                <td><?php echo $tuple['name']; ?></td>
                <td><?php echo $tuple['studentid']; ?></td>
                <td><?php echo $tuple['onid']; ?></td>
                <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                <td><?php echo $tuple['type']; ?></td>
                <td><?php echo $tuple['grade']; ?></td>
                <td><?php echo $tuple['response']; ?></td>
              </tr>
            <?php } ?>
          </tbody>
        </table>
      </div><br>

      <div class="ui divider"></div><br>

      <div id="nonstudent_responsediv">
        <h2>Resident/Visitor Responses</h2>
        <table class ="ui celled padded table" style="width: 25vw; height: 50vw; display: block; overflow-y:auto; overflow-x:auto">
          <thead>
            <tr>
              <th class="single line">Name</th>
              <th>Date and Time</th>
              <th>User Type</th>
              <th>Response</th>
            </tr>
          </thead>
          <tbody>
            <?php foreach ($nonStudentTuples as $tuple) { ?>
              <tr>
                <td><?php echo $tuple['name']; ?></td>
                <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                <td><?php echo $tuple['type']; ?></td>
                <td><?php echo $tuple['response']; ?></td>
              </tr>
            <?php } ?>
          </tbody>
        </table>
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
