<?php
$dbhost="oniddb.cws.oregonstate.edu";
$dbname="habibelo-db";
$dbuser="habibelo-db";
$dbpass="RcAbWdWDkpj7XNTL";

$mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
//Output any connection error
if ($mysqli->connect_error) {
  die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $userid = $_POST['userid'];

  $stmt = $mysqli->prepare("UPDATE ihc_users SET didsurvey=1 WHERE id=?");
  $stmt->bind_param('s', $userid);
  $stmt->execute();
  $res = $stmt->get_result();
  echo "SKIPSUCCESS";
  $res->close();
}

$mysqli->close();
?>
