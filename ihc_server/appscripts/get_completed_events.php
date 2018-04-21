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
      $stmt1 = $mysqli->prepare("SELECT id FROM ihc_users WHERE email=?");
      $stmt1->bind_param('s', $email);
      $stmt1->execute();
      $usersResult = $stmt->get_result();
      if ($usersResult->num_rows > 0) {
         $user = $usersResult->fetch_assoc();
         $userid = $user['id'];

         $stmt2 = $mysqli->prepare("SELECT * FROM ihc_completed_events WHERE userid=?");
         $stmt2->bind_param('i', $userid);
         $stmt2->execute();
         $result2 = $stmt2->get_result();
         if ($result2->num_rows > 0) {
            while ($row = $result2->fetch_assoc()) {
               $eventid = $row['eventid'];

               $stmt3 = $mysqli->prepare("SELECT * FROM ihc_events WHERE eventid=?");
               $stmt3->bind_param('i', $eventid);
               $stmt3->execute();
               $res = $stmt3->get_result();
               if ($res->num_rows > 0) {
                  $event = $res->fetch_assoc();
                  $data = json_encode($event);
                  echo $data;
                  echo "\\";
               }
            }
            $stmt3->close();
         }
         $stmt2->close();
      }
      $stmt1->close();
   }

   $mysqli->close();
?>
