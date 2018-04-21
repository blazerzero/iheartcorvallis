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
  echo "Connection failed!<br>";
}

$prizeid = $name = $level = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $email = $_POST["email"];
  $password = $_POST["password"];

  $iterations = 1000;

  $salt = openssl_random_pseudo_bytes(16);

  $hash = hash_pbkdf2("sha256",$password, $salt, $iterations, 50, false);

  // store salt with hash
  $hashandSalt = $salt . '|' . $hash;

  $stmt = $mysqli->prepare("INSERT INTO ihc_admin_users (email, password) VALUES (?, ?)");
  $stmt->bind_param('ss', $email, $hashandSalt);
  $stmt->execute();

  if ($stmt->error == "") {
    $message = "New User Created!";
  }
  else {
    $message = "Error Creating User!"; # error updating prize in database
    echo "<script type='text/javascript'>alert('$message');</script>";
  }

  $url = "../index.php";
  $mysqli->close();
  echo "<script type='text/javascript'>alert('$message');</script>";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";
  exit;

}

?>
