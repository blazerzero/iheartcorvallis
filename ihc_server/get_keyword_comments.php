<!DOCTYPE HTML>

<?php

$dbhost="oniddb.cws.oregonstate.edu";
$dbname="habibelo-db";
$dbuser="habibelo-db";
$dbpass="RcAbWdWDkpj7XNTL";

$mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
//Output any connection error
if ($mysqli->connect_error) {
  die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
  $message = "Unable to connect to the event database!";
  echo "<script type='text/javascript'>alert('$message');</script>";
  header("Location: ../index.html");
  exit;
}

$keyword = "";
$allTuples = $studentTuples = $nonStudentTuples = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $keyword = "%" . $_POST["keyword"] . "%";
  $stmt = $mysqli->prepare("SELECT * FROM ihc_feedback WHERE comment LIKE ?");
  $stmt->bind_param('s', $keyword);
  $stmt->execute();
  $fbResult = $stmt->get_result();
  if ($fbResult->num_rows > 0) {
    while ($fbRow = $fbResult->fetch_assoc()) {
      $userid = $fbRow['userid'];
      $dateandtime = $fbRow['dateandtime'];
      $rating = $fbRow['rating'];
      $comment = $fbRow['comment'];

      $stmt = $mysqli->prepare("SELECT * FROM ihc_users WHERE id=?");
      $stmt->bind_param('i', $userid);
      $stmt->execute();
      $userRes = $stmt->get_result();
      if ($userRes->num_rows > 0) {
        $userRow = $userRes->fetch_assoc();
        $name = $userRow['firstname'] . " " . $userRow['lastname'];

        $types = array('Domestic Student', 'International Student', 'Faculty', 'Resident', 'Visitor');
        $grades = array('N/A', 'Freshman', 'Sophomore', 'Junior', 'Senior', 'Graduate Student', 'Doctoral Student', 'Faculty');
        $type = $types[$userRow['type']];
        $grade = $grades[$userRow['grade']];

        $allTuples[] = array("userid" => $userid, "dateandtime" => $dateandtime, "name" => $name, "type" => $type, "rating" => $rating, "comment" => $comment);
        if ($userRow['type'] < 3) {
          $studentid = $userRow['studentid'];
          $onid = $userRow['onid'];
          $studentTuples[] = array("userid" => $userid, "dateandtime" => $dateandtime, "name" => $name, "studentid" => $studentid, "onid" => $onid, "type" => $type, "grade" => $grade, "rating" => $rating, "comment" => $comment);
        }
        else {
          $nonStudentTuples[] = array("userid" => $userid, "dateandtime" => $dateandtime, "name" => $name, "type" => $type, "rating" => $rating, "comment" => $comment);
        }
      }
    }
  }

  ?>

  <html>
  <head>
    <title>Analysis Center - I Heart Corvallis Administrative Suite</title>
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
      <div>
        <left class="sectionheader"><h1>Analysis Center</h1></left><br>
        <form action="./analyze.php">
          <button class="ui red button">
            <i class="arrow left icon"></i>
            Back to Selection Prompt
          </button>
        </form>
      </div>

      <?php if (count($allTuples) > 0) { ?>

        <div class="ui divider"></div><br>

        <div>
          <h2>Feedback Search Results: All Users</h2>
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
                  <td><?php echo $tuple['name']; ?></td>
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

        <?php if (count($studentTuples) > 0) { ?>

          <div>
            <h2>Feedback Search Results: Students and Faculty</h2>
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
                    <td><?php echo $tuple['name']; ?></td>
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
          </div><br>
        <?php } else { ?>
          <h4>No search results from students or faculty.</h4>
        <?php } ?>
        <div class="ui divider"></div><br>

        <?php if (count($nonStudentTuples) > 0)  { ?>

          <div>
            <h2>Feedback Search Results: Non-Students</h2>
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
                <?php foreach ($nonStudentTuples as $tuple) { ?>
                  <tr>
                    <td><?php echo $tuple['name']; ?></td>
                    <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                    <td><?php echo $tuple['type']; ?></td>
                    <td><?php if ($tuple['rating'] != 0) echo $tuple['rating']; ?></td>
                    <td><?php echo $tuple['comment']; ?></td>
                  </tr>
                <?php } ?>
              </tbody>
            </table>
          </div>
        <?php } else { ?>
          <h4>No search results from non-students.</h4>
        <?php }
      } else { ?>
        <h4>No search results.</h4>
      <?php } ?>
    </div>
  </body>
  </html>


  <?php
}
?>
