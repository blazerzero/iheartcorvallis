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

   $userid = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $email = $_POST["email"];
      $usersResult = $mysqli->query("SELECT id FROM ihc_users WHERE email='$email'");
      if ($usersResult->num_rows > 0) {
         $user = $usersResult->fetch_assoc();
         $userid = $user['id'];

         $result = $mysqli->query("SELECT * FROM ihc_completed_events WHERE userid='$userid'");
         if ($result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
               $eventid = $row['eventid'];
               $res = $mysqli->query("SELECT * FROM ihc_events WHERE eventid='$eventid'");
               if ($res->num_rows > 0) {
                  $event = $res->fetch_assoc();
                  $data = json_encode($event);
                  echo $data;
                  echo "\\";
               }
            }
         }
      }
   }

   mysqli_close($con);
?>
