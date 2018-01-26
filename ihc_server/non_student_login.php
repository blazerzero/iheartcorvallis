<?php
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

   $email = $password = $row = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $email = $_POST['email'];
      $password = $_POST['password'];

      $result = $mysqli->query("SELECT email, password FROM ihc_users");
      if ($result->num_rows > 0) {
         while ($user = $result->fetch_assoc()) {
            if ($user["email"] == $email && $user["password"] == $password) {
               $isAuth = True;
               break;
            }
         }
      }
      if ($isAuth == True) {
         $result = $mysqli->query("SELECT firstname, lastname, id FROM ihc_users WHERE email='$email' AND password='$password'");

         if ($result->num_rows == 1) {
            while ($row = $result->fetch_assoc()) {
               echo "LOGINSUCCESS";
            }
         }
         else if ($result->num_rows == 0) {
            echo "NOACCOUNTERROR"; # account does not exist
         }
         else if ($result->num_rows > 1) {
            echo "DUPACCOUNTERROR";  # more than one account with this email/password
         }
         else {
            echo "SEARCHERROR"; # error looking for account
         }
      }
      else {
         echo "AUTHERROR"; # unable to authenticate email/password
      }
   }
   mysqli_close($con);
?>
