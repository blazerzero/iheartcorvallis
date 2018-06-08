<?php

/*************************************/
/* RETRIEVE THE USER'S PASSWORD HASH */
/*************************************/

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

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $email = $_POST['email'];   // get the user's email via POST

  /* GET THE USER'S PASSWORD HASH */
  $stmt = $mysqli->prepare("SELECT password FROM ihc_users WHERE email=?");
  $stmt->bind_param('s', $email);
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {    // an account with that email exists

    $row = $result->fetch_assoc();
    echo $row['password'];    // send the password hash to the app

  }
  else {    // no account exists with that email
    echo "NOACCOUNTERROR";    // send an error message to the app
  }
  $stmt->close();
}

$mysqli->close();
?>
