<?php

ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');

$dbhost="oniddb.cws.oregonstate.edu";
$dbname="habibelo-db";
$dbuser="habibelo-db";
$dbpass="RcAbWdWDkpj7XNTL";

$alreadyExists = False;

$mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
//Output any connection error
if ($mysqli->connect_error) {
  die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
}

$userid = $dateandtime = $questionid = $response = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $userid = $_POST['userid'];
  $dateandtime = date("Y-m-d H:i:s");

  $questionid = $_POST['questionid'];
  $response = $_POST['response'];
  $stmt1 = $mysqli->prepare("INSERT INTO ihc_survey_responses (userid, dateandtime, questionid, response) VALUES (?, ?, ?, ?)");
  $stmt1->bind_param('isis', $userid, $dateandtime, $questionid, $response);
  $stmt1->execute();
  if ($stmt1->error == "") {
    $stmt2 = $mysqli->prepare("UPDATE ihc_users SET didsurvey = 1 WHERE id=?");
    $stmt2->bind_param('s', $userid);
    $stmt2->execute();
    if ($stmt2->error == "") {
      echo "ADDSUCCESS";
    }
    else {
      echo "ADDERROR";
    }
    $stmt2->close();
  }
  else {
    echo "ADDERROR";
  }
  $stmt1->close();
}

$mysqli->close();
?>
