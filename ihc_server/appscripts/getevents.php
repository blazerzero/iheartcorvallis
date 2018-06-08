<?php

/***********************/
/* RETRIEVE THE EVENTS */
/***********************/

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
  $result = $mysqli->query("SELECT * FROM ihc_events");   // retrieve all events
  if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {   // for each event
      $data = json_encode($row);    // encode the event into JSON
      echo $data;   // send the event to the app
      echo "\\";    // add a delimiter to the event
    }
  }
}

$mysqli->close();
?>
