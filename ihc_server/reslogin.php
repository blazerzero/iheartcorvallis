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
       echo "Connection failed!<br>";
   }
   else {
      echo "Connection succesful!<br>";
   }

   $email = "habibelo@oregonstate.edu";
   $password = "Password123";
   $row = "";

   //$email = $_POST['email'];
   //$password = $_POST['password'];

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
      $result = $mysqli->query("SELECT firstname, lastname FROM ihc_users WHERE email='$email' AND password='$password'");

      if ($result->num_rows == 1) {
         while ($row = $result->fetch_assoc()) {
            echo "User: " . $row["firstname"] . " " . $row["lastname"] . "<br>";
            $data = json_encode($row, JSON_PRETTY_PRINT);
            echo $data;
         }
      }
      else if ($result->num_rows == 0) {
         echo "ERROR: Invalid email/password combination!<br>";
      }
      else if ($result->num_rows > 1) {
         echo "ERROR: More than one account with this email and password!<br>";
      }
      else {
         echo "ERROR logging in.<br>";
      }
   }
   mysqli_close($con);
?>
