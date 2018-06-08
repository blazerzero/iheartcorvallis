<?php

/*********************************/
/* RETRIEVE THE RESOURCE MARKERS */
/*********************************/

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

// Not user input so no sanitizing
if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET THE RESOURCE MARKERS */
  $stmt = $mysqli->prepare("SELECT * FROM ihc_resources");
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {   // for each resource marker
      $data = json_encode($row);    // encode the resource marker into JSON
      echo $data;   // send the resource marker to the app
      echo "\\";    // add a delimiter to the resource marker
    }
  }
  $stmt->close();
}

$mysqli->close();
?>
