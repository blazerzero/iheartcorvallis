<?php

/*************************************************************/
/* SAVE THE USER'S EXTRA ACCOUNT INFORMATION TO THE DATABASE */
/*************************************************************/

ini_set('display_errors', 1);
error_reporting(E_ERROR);
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

$id = $studentid = $onid = $grade = $type = $birthdate = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET VALUES VIA POST */
  $id = $_POST['userid'];
  $studentid = $_POST['studentid'];
  $onid = $_POST['onid'];
  $grade = $_POST['grade'];
  $type = $_POST['type'];
  $birthdate = substr($_POST['birthdate'], 0, 10) . " 00:00:00";    // format birthdate as SQL DATETIME data type

  /* ADD EXTRA USER INFORMATION TO USER TABLE FOR THAT USER */
  $stmt = $mysqli->prepare("UPDATE ihc_users SET studentid=?, onid=?, grade=?, type=?, birthdate=? WHERE id=?");
  $stmt->bind_param('ssiisi', $studentid, $onid, $grade, $type, $birthdate, $id);
  $stmt->execute();
  if ($stmt->error == "") {   // successfully saved extra user information
    echo "ADDSUCCESS";    // send success message to the app
  }
  else {    // error saving extra user information
    echo "ADDERROR";    // send error message to the app
  }
  $stmt->close();
}
$mysqli->close();
?>
