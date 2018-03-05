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

function checkAuth() {
   //echo session_id();
   if (isset($_SESSION["id"]) && $_SESSION["id"] != null) {
      return $_SESSION["id"];
   }
   else {
      return "";
   }
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
   $email = $_POST["email"];
   $password = $_POST["password"];

   $result = $mysqli->query("SELECT email FROM ihc_admin_users WHERE email='$email' AND password='$password'");
   if ($result->num_rows > 0) {
      //$_SESSION["id"] = $email;
      /*$user = $result->fetch_assoc();
      if ($email == $user["email"]) {*/
         //$message = "Correct login credentials!"; # correct login credentials
         //echo "<script type='text/javascript'>alert('$message');</script>";
         $url = "../index.php";
         echo "<script type='text/javascript'>document.location.href = '$url';</script>";
         /*echo $_SESSION["id"]."<br>";
         $id = checkAuth();
         echo "ID: ".$id."<br>";*/
      //}

      //$message = "Incorrect login credentials!"; # error logging in

   }
   else {
      $url = "../admin_auth.php";
      //echo "<script type='text/javascript'>alert('$message');</script>";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
   }
}

$mysqli->close();

?>
