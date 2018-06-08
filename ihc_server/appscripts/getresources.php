<?php

/**************************/
/* RETRIEVE THE RESOURCES */
/**************************/

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

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET THE RESOURCES */
  $stmt = $mysqli->prepare("SELECT * FROM ihc_resource_info");
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {   // for each resource
      $data = json_encode($row);    // encode the resource into JSON
      echo $data;   // send the resource to the app
      echo "\\";    // add a delimiter to the app
    }
  }
  $stmt->close();
}

$mysqli->close();
?>
