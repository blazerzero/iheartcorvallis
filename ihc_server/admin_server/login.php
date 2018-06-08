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

session_start();

function checkAuth($id) {
  //echo session_id();
  if ($id != null) {
    return $id;
  }
  else {
    return "";
  }
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET VALUES VIA POST */
  $email = $_POST["email"];
  $password = $_POST["password"];

  /* CHECK IF THE ACCOUNT EXISTS */
  $stmt = $mysqli->prepare("SELECT password FROM ihc_admin_users WHERE email=?");
  $stmt->bind_param('s', $email);
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {    // the account exists

    $row = $result->fetch_assoc();
    $pHash = $row['password'];        // user's stored password that we must compare with

    /* HASH PASSWORD INPUT AND COMPARE TO STORED HASH */
    $iterations = 1000;

    // need to get the salt from the hash
    $storedpHash = explode("|", $pHash);// salt of stored password
    $hash = hash_pbkdf2("sha256",$password, $storedpHash[0], $iterations, 50, false);

    $url = "";
    if (!strcmp($hash,$storedpHash[1])) {   // the hashes match
      $_SESSION["id"] = $email;   // set session ID to the user's email
      $url = "../index.php";
    }
    else {    // the hashes don't match; incorrect password input
      $message = "Incorrect email/password combination!";
      echo "<script type='text/javascript'>alert('$message');</script>";    // show error alert with message
      $url = "../admin_auth.php";
    }
    echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url

  }
  else {    // there is no authorized user with the input email address
    $message = "There is no authorized user with that email address!";
    echo "<script type='text/javascript'>alert('$message');</script>";    // show error alert with message
    $url = "../admin_auth.php";
    echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url
  }
}

$mysqli->close();

?>
