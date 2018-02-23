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

   $userid = $eventid = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $userid = $_POST["userid"];
      $eventid = $_POST["eventid"];
	  $stmt = $mysqli->query("INSERT INTO ihc_completed_events (userid, eventid) VALUES (?, ?)");
	  $stmt->bind_param("ii", $userid, $eventid);
	  $stmt->execute();
	  $stmt->close();
      //$result = $mysqli->query("INSERT INTO ihc_completed_events (userid, eventid) VALUES ('$userid', '$eventid')");
   }

   mysqli_close($con);
?>
