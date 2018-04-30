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

  $stmt = $mysqli->prepare("SELECT U.*, R.dateandtime, R.response FROM ihc_users U, ihc_survey_responses R WHERE R.userid=U.id AND questionid=?");
  $stmt->bind_param('i', $id);
  $stmt->execute();
  $res = $stmt->get_result();
  if ($res->num_rows > 0) {
    while ($row = $res->fetch_assoc()) {
      $dateandtime = $row['dateandtime'];
      $response = $row['response'];
      $name = $row['firstname'] . " " . $row['lastname'];
      /*$type = "";
      $grade = "";
      if ($row['type'] == 0) $type = "Domestic Student";
      else if ($row['type'] == 1) $type = "International Student";
      else if ($row['type'] == 2) $type = "Faculty";
      else if ($row['type'] == 3) $type = "Resident";
      else if ($row['type'] == 4) $type = "Visitor";
      if ($row['grade'] == 0) $grade = "N/A";
      else if ($row['grade'] == 1) $grade = "Freshman";
      else if ($row['grade'] == 2) $grade = "Sophomore";
      else if ($row['grade'] == 3) $grade = "Junior";
      else if ($row['grade'] == 4) $grade = "Senior";
      else if ($row['grade'] == 5) $grade = "Graduate Student";
      else if ($row['grade'] == 6) $grade = "Doctoral Student";
      else if ($row['grade'] == 7) $grade = "Faculty";*/
      $types = array('Domestic Student', 'International Student', 'Faculty', 'Resident', 'Visitor');
      $grades = array('N/A', 'Freshman', 'Sophomore', 'Junior', 'Senior', 'Graduate Student', 'Doctoral Student', 'Faculty');
      $type = $types[$row['type']];
      $grade = $grades[$row['grade']]; 

      $allTuples[] = array("userid" => $userid, "dateandtime" => $dateandtime, "name" => $name, "type" => $type, "response" => $response);
      if ($row['type'] < 2) {
        $studentid = $row['studentid'];
        $onid = $row['onid'];
        $studentTuples[] = array("userid" => $userid, "dateandtime" => $dateandtime, "name" => $name, "studentid" => $studentid, "onid" => $onid, "grade" => $grade, "type" => $type, "response" => $response);
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
                <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
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
                <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
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
