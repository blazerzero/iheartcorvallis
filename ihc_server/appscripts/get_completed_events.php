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

$userid = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $userid = $_POST['userid'];
  $stmt = $mysqli->prepare("SELECT E.* FROM ihc_events E, ihc_completed_events CE WHERE E.eventid=CE.eventid AND CE.userid=?");
  $stmt->bind_param('i', $userid);
  $stmt->execute();
  $res = $stmt->get_result();
  if ($res->num_rows > 0) {
    while ($event = $res->fetch_assoc()) {
      $data = json_encode($event);
      echo $data;
      echo "\\";
    }
  }
  $stmt->close();
}

$mysqli->close();
?>
