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

   $result = $mysqli->query("SELECT name, location, dateandtime, description, link1, link2, link3 FROM ihc_events");
   if ($result->num_rows > 0) {
      while ($row = $result->fetch_assoc()) {
         $data = json_encode($row);
         echo $data;
      }
   }

   mysqli_close($con);
?>
