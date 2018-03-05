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

   $result = $mysqli->query("SELECT password FROM ihc_admin_users WHERE email='$email'");
   if ($result->num_rows > 0) {
	   
	   $row = $result->fetch_assoc();
	   
	   // user's stored password that we must compare with
	   $pHash = $row['password'];
	   
	   $iterations = 1000;
	   
	   // use this to salt the original password
	   //$salt = openssl_random_pseudo_bytes(16);
	   
	   // need to get the salt from the hash
	   $storedpHash = explode("|", $pHash);// salt of stored password
	   
	   $hash = hash_pbkdf2("sha256",$password, $storedpHash[0], $iterations, 50);
	   
	   if (strcmp($hash,$storedpHash[1])) {
	     // logic after checking hash password
		 $_SESSION["id"] = $email;
	  
	  
         //$sessionid = $_SESSION["id"];
         //session_regenerate_id(true);
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
	   }
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
