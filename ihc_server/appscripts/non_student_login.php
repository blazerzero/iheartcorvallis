<?php

ini_set('display_errors', 1);
error_reporting(E_ALL);
ini_set('memory_limit', '1G');

$dbhost="oniddb.cws.oregonstate.edu";
$dbname="habibelo-db";
$dbuser="habibelo-db";
$dbpass="RcAbWdWDkpj7XNTL";

$isAuth = False;

$mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
//Output any connection error
if ($mysqli->connect_error) {
  die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
}

$email = $row = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $email = $_POST['email'];
  $stmt1 = $mysqli->prepare("SELECT email FROM ihc_users WHERE email=?");
  $stmt1->bind_param('s', $email);
  $stmt1->execute();
  $result1 = $stmt1->get_result();

  if ($result1->num_rows > 0) {
    $stmt2 = $mysqli->prepare("SELECT * FROM ihc_users WHERE email=?");
    $stmt2->bind_param('s', $email);
    $stmt2->execute();
    $result2 = $stmt2->get_result();
    $row = $result2->fetch_assoc();
    $data = json_encode($row);
    echo $data;
    $stmt2->close();
  }
  else {
    echo "AUTHERROR"; # account not found (email not found)
  }
  $stmt1->close();
}
$mysqli->close();
?>
