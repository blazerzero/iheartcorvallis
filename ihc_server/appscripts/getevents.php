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

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $result = $mysqli->query("SELECT name, location, address, latitude, longitude, dateandtime, description, image, link1, link2, link3 FROM ihc_events");
      if ($result->num_rows > 0) {
         while ($row = $result->fetch_assoc()) {
            echo $row["name"] . "\\";
            echo $row["location"] . "\\";
            echo $row["dateandtime"] . "\\";
            echo $row["description"] . "\\";
            //echo $row["image"] . "\\";
            echo ";";
         }
      }
   }

   mysqli_close($con);
?>
