<?php

/*********************************************/
/* ADD A USER'S APP FEEDBACK TO THE DATABASE */
/*********************************************/

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

$userid = $dateandtime = $rating = $comment = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET VALUES VIA POST */
  $userid = $_POST['userid'];
  $dateandtime = date("Y-m-d H:i:s");   // get current timestamp
  $rating = $_POST['rating'];
  $comment = $_POST['comment'];

  /* ADD APP FEEDBACK TO DATABASE */
  $stmt = $mysqli->prepare("INSERT INTO ihc_feedback (userid, dateandtime, rating, comment) VALUES (?, ?, ?, ?)");
  $stmt->bind_param('isis', $userid, $dateandtime, $rating, $comment);
  $stmt->execute();
  if ($stmt->error == "") {   // successfully added app feedback to database
    echo "ADDSUCCESS";    // send success message to the app
  }
  else {    // error adding app feedback to database
    echo "ADDERROR";    // send error message to the app
  }
  $stmt->close();
}

$mysqli->close();
?>
