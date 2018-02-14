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
      $email = $_POST['email'];
      $result = $mysqli->query("SELECT password FROM ihc_users WHERE email='$email'");
      if ($result == True) {
         $row = $result->fetch_assoc();
         echo $row['password'];
      }
   }

   mysqli_close($con);
?>
