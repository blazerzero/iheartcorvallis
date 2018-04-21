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
  $email = $_POST["email"];
  $password = $_POST["password"];

  $stmt = $mysqli->prepare("SELECT password FROM ihc_admin_users WHERE email=?");
  $stmt->bind_param('s', $email);
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {

    $row = $result->fetch_assoc();

    // user's stored password that we must compare with
    $pHash = $row['password'];
    $iterations = 1000;

    // need to get the salt from the hash
    $storedpHash = explode("|", $pHash);// salt of stored password
    $hash = hash_pbkdf2("sha256",$password, $storedpHash[0], $iterations, 50, false);

    $url = "";
    if (!strcmp($hash,$storedpHash[1])) {
      // logic after checking hash password
      $_SESSION["id"] = $email;
      $url = "../index.php";
    }
    else {
      $message = "Incorrect email/password combination!";
      echo "<script type='text/javascript'>alert('$message');</script>";
      $url = "../admin_auth.php";
    }
    echo "<script type='text/javascript'>document.location.href = '$url';</script>";

  }
  else {
    $message = "There is no authorized user with that email address!";
    echo "<script type='text/javascript'>alert('$message');</script>";
    $url = "../admin_auth.php";
    echo "<script type='text/javascript'>document.location.href = '$url';</script>";
  }
}

$mysqli->close();

?>
