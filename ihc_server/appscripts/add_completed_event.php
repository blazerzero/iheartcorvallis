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

$userid = $eventid = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $userid = $_POST["userid"];
  $eventid = $_POST["eventid"];
  $rating = $_POST["rating"];
  $comment = $_POST["comment"];
  $dateandtime = date("Y-m-d H:i:s");

  $stmt = $mysqli->prepare("INSERT INTO ihc_completed_events (userid, eventid, dateandtime, rating, comment) VALUES (?, ?, ?, ?, ?)");
  $stmt->bind_param('iisis', $userid, $eventid, $dateandtime, $rating, $comment);
  $stmt->execute();
  if ($stmt->get_error == "") {
    $stmt2 = $mysqli->prepare("SELECT * FROM ihc_completed_events WHERE userid=?");
    $stmt2->bind_param('i', $userid);
    $stmt2->execute();
    if ($stmt2->error == "") {
      $result = $stmt2->get_result();
      $stampcount = $result->num_rows;

      $stmt3 = $mysqli->prepare("UPDATE ihc_users SET stampcount=? WHERE id=?");
      $stmt3->bind_param('ii', $stampcount, $userid);
      $stmt3->execute();
      if ($stmt3->error == "") {
        echo "COMPLETED EVENT ADDED";
      }
      $stmt3->close();
    }
    $stmt2->close();
  }
  else {
    echo "ADDERROR";
  }
  $stmt->close();
}

$mysqli->close();
?>
