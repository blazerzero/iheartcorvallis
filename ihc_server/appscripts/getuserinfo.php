<?php

/**************************************/
/* GET THE USER'S ACCOUNT INFORMATION */
/**************************************/

ini_set('display_errors', 1);
error_reporting(E_ALL);
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

$email = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $email = $_POST['email'];   // get the user's email via POST

  /* GET THE USER'S INFORMATION */
  $stmt = $mysqli->prepare("SELECT * FROM ihc_users WHERE email=?");
  $stmt->bind_param('s', $email);
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $data = json_encode($row);    // encode the user's information into JSON
    echo $data;   // send the user's information to the app
  }
  $stmt->close();
}

$mysqli->close();
?>
