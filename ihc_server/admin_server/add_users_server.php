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

  /* GET VALUES VIA POST */
  $email = $_POST["email"];
  $password = $_POST["password"];

  /* HASH PASSWORD */
  $iterations = 1000;
  $salt = openssl_random_pseudo_bytes(16);
  $hash = hash_pbkdf2("sha256",$password, $salt, $iterations, 50, false);
  $hashandSalt = $salt . '|' . $hash;     // store salt with hash

  /* ADD ADMIN USER TO DATABASE */
  $stmt = $mysqli->prepare("INSERT INTO ihc_admin_users (email, password) VALUES (?, ?)");
  $stmt->bind_param('ss', $email, $hashandSalt);
  $stmt->execute();

  if ($stmt->error == "") {   // successfully created administrative user
    $message = "New user created!";
  }
  else {    // error creating administrative user
    $message = "Error creating user!";
    echo "<script type='text/javascript'>alert('$message');</script>";
  }

  $url = "../index.php";
  $stmt->close();
  $mysqli->close();
  echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url
  exit;

}

$mysqli->close();

?>
