<?php

/*******************************************/
/* RECORD THAT THE USER SKIPPED THE SURVEY */
/*******************************************/

ini_set('display_errors', 1);
error_reporting(E_ALL);
ini_set('memory_limit', '1G');

$dbhost="oniddb.cws.oregonstate.edu";
$dbname="habibelo-db";
$dbuser="habibelo-db";
$dbpass="RcAbWdWDkpj7XNTL";

$mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
//Output any connection error
if ($mysqli->connect_error) {
  die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $userid = $_POST['userid'];   // get user ID via POST

  /* RECORD THAT THE USER SKIPPED THE SURVEY */
  $stmt = $mysqli->prepare("UPDATE ihc_users SET didsurvey=1 WHERE id=?");
  $stmt->bind_param('s', $userid);
  $stmt->execute();
  if ($stmt->error == "") {   // successfully recorded that the user skipped the survey
    echo "SKIPSUCCESS";   // send success message to the app
  }
  else {    // error recording that the user skipped the survey
    echo "SKIPERROR";   // send error message to the app
  }
  $stmt->close();
}

$mysqli->close();
?>
