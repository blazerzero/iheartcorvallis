<?php
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
  $result = $mysqli->query("SELECT * FROM ihc_about");
  if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $data = json_encode($row);
    echo $data;
  }
}

$mysqli->close();
?>
