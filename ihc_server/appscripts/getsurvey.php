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
  $stmt = $mysqli->prepare("SELECT * FROM ihc_survey");
  $stmt->execute();
  $result = $stmt->get_result();

  if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
      $data = json_encode($row);
      echo $data;
      echo "\\";
    }
  }
  $stmt->close();
}

$mysqli->close();
?>
