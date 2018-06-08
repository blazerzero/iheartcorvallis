<?php

/*************************************************/
/* ADD A COMPLETED EVENT LISTING TO THE DATABASE */
/*************************************************/

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

  /* GET VALUES VIA POST */
  $userid = $_POST["userid"];
  $eventid = $_POST["eventid"];
  $rating = $_POST["rating"];
  $comment = $_POST["comment"];
  $dateandtime = date("Y-m-d H:i:s");   // get current timestamp

  /* ADD COMPLETED EVENT LISTING TO DATABASE */
  $stmt = $mysqli->prepare("INSERT INTO ihc_completed_events (userid, eventid, dateandtime, rating, comment) VALUES (?, ?, ?, ?, ?)");
  $stmt->bind_param('iisis', $userid, $eventid, $dateandtime, $rating, $comment);
  $stmt->execute();
  if ($stmt->get_error == "") {   // successfully added completed event listing to database

    /* GET NUMBER OF COMPLETED EVENTS FOR THAT USER */
    $stmt2 = $mysqli->prepare("SELECT * FROM ihc_completed_events WHERE userid=?");
    $stmt2->bind_param('i', $userid);
    $stmt2->execute();
    if ($stmt2->error == "") {    // successfully retrieved number of completed events
      $result = $stmt2->get_result();
      $stampcount = $result->num_rows;

      /* UPDATE STAMP COUNT FOR THE USER */
      $stmt3 = $mysqli->prepare("UPDATE ihc_users SET stampcount=? WHERE id=?");
      $stmt3->bind_param('ii', $stampcount, $userid);
      $stmt3->execute();
      if ($stmt3->error == "") {    // successfully updated stamp count
        echo "COMPLETED EVENT ADDED";   // send success message to the app
      }
      $stmt3->close();
    }
    $stmt2->close();
  }
  else {    // error adding completed event listing to database
    echo "ADDERROR";    // send error message to the app
  }
  $stmt->close();
}

$mysqli->close();
?>
