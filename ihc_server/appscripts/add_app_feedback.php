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
}

$userid = $dateandtime = $rating = $comment = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $userid = $_POST['userid'];
  $dateandtime = date("Y-m-d H:i:s");
  $rating = $_POST['rating'];
  $comment = $_POST['comment'];
  $stmt = $mysqli->prepare("INSERT INTO ihc_feedback (userid, dateandtime, rating, comment) VALUES (?, ?, ?, ?)");
  $stmt->bind_param('isis', $userid, $dateandtime, $rating, $comment);
  $stmt->execute();
  if ($stmt->error == "") {
    echo "ADDSUCCESS";
  }
  else {
    echo "ADDERROR";
  }
  $stmt->close();
}

$mysqli->close();
?>
