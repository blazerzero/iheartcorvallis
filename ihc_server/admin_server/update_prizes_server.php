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

$prizeid = $name = $level = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $prizeid = $_POST["prizeid"];
  $name = $_POST["name"];
  $levelVal = $_POST["level"];

  if ($levelVal == "1") {
    $level = "gold";
  }
  else if ($levelVal == "2") {
    $level = "silver";
  }
  else if ($levelVal == "3") {
    $level = "bronze";
  }

  $stmt = $mysqli->prepare("UPDATE ihc_prizes SET name=?, level=? WHERE prizeid=?");
  $stmt->bind_param('ssi', $name, $level, $prizeid);
  $stmt->execute();

  if ($stmt->error == "") {
    $message = "Prize has been updated!";
  }
  else {
    $message = "Error updating prize!"; # error updating prize in database
  }
  $url = "../manage_prizes.php";

  $stmt->close();
  $mysqli->close();
  echo "<script type='text/javascript'>alert('$message');</script>";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
$mysqli->close();

?>
