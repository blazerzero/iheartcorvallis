<?php

//require "./login.php";

//if (isset($_SESSION["id"]) && $_SESSION["id"] != null) {
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
   else {
      echo "Connection succesful!<br>";
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

     //echo $email."<br>".$hashandSalt."<br>";

      $result = $mysqli->query("INSERT INTO ihc_admin_users (email, password) VALUES ('$email', '$hashandSalt')");

      if ($result == True) {
         $message = "New User Created!";
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      else {
         $message = "Error Creating User!"; # error updating prize in database
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      $url = "../index.php";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
   }
   $mysqli->close();
/*}
else {
   $url = "../admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}*/
?>