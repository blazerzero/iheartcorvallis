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

   $userid = $eventid = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $userid = $_POST["userid"];
      $grade = $_POST["grade"];
      $age = $_POST["age"];
	   //$stmt = $mysqli->prepare("INSERT INTO ihc_completed_events (userid, eventid) VALUES (?, ?)");
	   //$stmt->bind_param('ii', $userid, $eventid);
	   //$result = $stmt->execute();
      $result = $mysqli->query("UPDATE ihc_users SET grade='$grade', age='$age' WHERE id='$userid'");
      if ($result == True) {
         echo "UPDATESUCCESS";
      }
      else {
         echo "UPDATEERROR";
      }
	   //$stmt->close();
   }

   $mysqli->close();
?>