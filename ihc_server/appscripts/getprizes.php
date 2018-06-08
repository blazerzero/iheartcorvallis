<?php

/***********************/
/* RETRIEVE THE PRIZES */
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

  /* GET THE PRIZES */
  $stmt = $mysqli->prepare("SELECT * FROM ihc_prizes");
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {   // for each prize
      $data = json_encode($row);    // encode the prize into JSON
      echo $data;   // send the prize to the app
      echo "\\";    // add a delimiter to the prize
    }
  }
  $stmt->close();
}

$mysqli->close();
?>
