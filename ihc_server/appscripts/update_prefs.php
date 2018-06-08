<?php

/*****************************************/
/* UPDATE THE USER'S ACCOUNT INFORMATION */
/*****************************************/

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

$userid = $firstname = $lastname = $email = $type = $grade = $birthdate = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET VALUES VIA POST */
  $userid = $_POST["userid"];
  $firstname = $_POST["firstname"];
  $lastname = $_POST["lastname"];
  $email = $_POST["email"];
  $type = $_POST["type"];
  $grade = $_POST["grade"];
  $birthdate = $_POST["birthdate"];

  /* UPDATE THE USER'S ACCOUNT INFORMATION */
  $stmt = $mysqli->prepare("UPDATE ihc_users SET firstname=?, lastname=?, email=?, grade=?, type=?, birthdate=? WHERE id=?");
  $stmt->bind_param('sssiisi', $firstname, $lastname, $email, $grade, $type, $birthdate, $userid);
  $stmt->execute();
  if ($stmt->error == "") {   // successfully updated the user's account information
    echo "UPDATESUCCESS";   // send success message to the app
  }
  else {    // error updating the user's account information
    echo "UPDATEERROR";   // send error message to the app
  }
  $stmt->close();
}

$mysqli->close();
?>
