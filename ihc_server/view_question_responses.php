<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $id = $_GET['id'];
  $allTuples = $studentTuples = array();
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

  $stmt = $mysqli->prepare("SELECT * FROM ihc_survey_responses WHERE questionid=?");
  $stmt->bind_param('i', $id);
  $stmt->execute();
  $surveyres = $stmt->get_result();
  if ($surveyres->num_rows > 0) {
    while ($surveyrow = $surveyres->fetch_assoc()) {
      $userid = $surveyrow['userid'];
      $dateandtime = $surveyrow['dateandtime'];
      $response = $surveyrow['response'];
      $stmt = $mysqli->prepare("SELECT * FROM ihc_users WHERE id=?");
      $stmt->bind_param('i', $userid);
      $stmt->execute();
      $userres = $stmt->get_result();
      if ($userres->num_rows > 0) {
        $userrow = $userres->fetch_assoc();
        $name = $userrow['firstname'] . " " . $userrow['lastname'];
        $type = "";
        $grade = "";
        if ($userrow['type'] == 0) $type = "Domestic Student";
        else if ($userrow['type'] == 1) $type = "International Student";
        else if ($userrow['type'] == 2) $type = "Faculty";
        else if ($userrow['type'] == 3) $type = "Resident";
        else if ($userrow['type'] == 4) $type = "Visitor";
        if ($userrow['grade'] == 0) $grade = "N/A";
        else if ($userrow['grade'] == 1) $grade = "Freshman";
        else if ($userrow['grade'] == 2) $grade = "Sophomore";
        else if ($userrow['grade'] == 3) $grade = "Junior";
        else if ($userrow['grade'] == 4) $grade = "Senior";
        else if ($userrow['grade'] == 5) $grade = "Graduate Student";
        else if ($userrow['grade'] == 6) $grade = "Doctoral Student";
        else if ($userrow['grade'] == 7) $grade = "Faculty";

        $allTuples[] = array("userid" => $userid, "dateandtime" => $dateandtime, "name" => $name, "type" => $type, "response" => $response);
        if ($userrow['type'] < 2) {
          $studentid = $userrow['studentid'];
          $onid = $userrow['onid'];
          $studentTuples[] = array("userid" => $userid, "dateandtime" => $dateandtime, "name" => $name, "studentid" => $studentid, "onid" => $onid, "grade" => $grade, "type" => $type, "response" => $response);
        }
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
    });
    </script>
  </head>
  <body>
    <div class="siteheader" id="siteheader"></div>

    <div class="mainbody">
      <left class="sectionheader"><h1>View Question Responses</h1></left><br>
      <div class="ui divider"></div><br>

      <div>
        <h2>General</h2>
        <h4>Question: <?php echo $question; ?></h4>
        <h4>Choices:
          <?php
          for ($i = 0; $i < count($choices) - 1; $i++) echo $choices[$i]. ", ";
          echo $choices[count($choices) - 1];
          ?>
        </h4>
        <h4>Number of Responses: <?php echo count($allTuples); ?></h4>
        <h4>Number of Student/Faculty Responses: <?php echo count($studentTuples); ?></h4>
      </div><br>

      <div class="ui divider"></div><br>

      <div>
        <h2>All Responses</h2>
        <table class ="ui celled padded table">
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
                <td><?php echo $tuple['dateandtime']; ?></td>
                <td><?php echo $tuple['type']; ?></td>
                <td><?php echo $tuple['response']; ?></td>
              </tr>
            <?php } ?>
          </tbody>
        </table>
      </div>

      <div class="ui divider"></div><br>

      <div>
        <h2>Student Responses</h2>
        <table class ="ui celled padded table">
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
                <td><?php echo $tuple['dateandtime']; ?></td>
                <td><?php echo $tuple['type']; ?></td>
                <td><?php echo $tuple['grade']; ?></td>
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
