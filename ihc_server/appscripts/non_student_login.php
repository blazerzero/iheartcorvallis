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

   $email = $row = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $email = $_POST['email'];
      $result = $mysqli->query("SELECT email FROM ihc_users WHERE email='$email'");
      if ($result->num_rows > 0) {
            $isAuth = True;
      }
      if ($isAuth == True) {
         $result = $mysqli->query("SELECT firstname, lastname, email, id, stampcount FROM ihc_users WHERE email='$email'");
         $row = $result->fetch_assoc();
         $data = json_encode($row);
         echo $data;
      }
      else {
         echo "AUTHERROR"; # unable to authenticate email/password
      }
   }
   mysqli_close($con);
?>
