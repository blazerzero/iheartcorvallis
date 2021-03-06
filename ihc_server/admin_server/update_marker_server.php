<?php

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
  echo "Connection failed!<br>";
}

$prizeid = $name = $type = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET VALUES VIA POST */
  $id = $_POST["id"];
  $name = $_POST["name"];
  $address = $_POST["address"];
  $type = $_POST["type"];

  /* UPDATE RESOURCE MAP MARKER */
  $stmt = $mysqli->prepare("UPDATE ihc_resources SET name=?, address=?, type=? WHERE id=?");
  $stmt->bind_param('sssi', $name, $address, $type, $id);
  $stmt->execute();

  if ($stmt->error == "") {   // succesfully updated resource marker
    $message = "Resource marker has been updated!";
  }
  else {    // error updating resource marker
    $message = "Error updating resource marker!";
  }
  $url = "../manage_resource_map.php";

  $stmt->close();
  $mysqli->close();
  echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url
}

$mysqli->close();

?>
