<?php

/************************************/
/* RETRIEVE THE ABOUT PAGE CONTENTS */
/************************************/

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

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET ABOUT PAGE INFORMATION */
  $result = $mysqli->query("SELECT * FROM ihc_about");
  if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $data = json_encode($row);    // format the data as JSON
    echo $data;   // send data to the app
  }
}

$mysqli->close();
?>
