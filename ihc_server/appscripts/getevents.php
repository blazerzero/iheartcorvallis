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
      $result = $mysqli->query("SELECT name, location, address, latitude, longitude, dateandtime, description, link1, link2, link3 FROM ihc_events");
      if ($result->num_rows > 0) {
         while ($row = $result->fetch_assoc()) {
            if (strpos($row['link1'], '/') === true) {
               $row['link1'] = "";
            }
            if (strpos($row['link2'], '/') === true) {
               $row['link1'] = "";
            }
            if (strpos($row['link3'], '/') === true) {
               $row['link1'] = "";
            }
            $data = json_encode($row);
            echo $data;
            echo "\\";
         }
      }
   }

   mysqli_close($con);
?>
