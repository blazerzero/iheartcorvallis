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

   // Not user input so no sanitizing
   if ($_SERVER["REQUEST_METHOD"] == "POST") {
	  $stmt = $mysqli->prepare("SELECT * FROM ihc_resources");
	  $stmt->execute();
	  $result = $stmt->get_result();
      //$result = $mysqli->query("SELECT * FROM ihc_resources");
      if ($result->num_rows > 0) {
         while ($row = $result->fetch_assoc()) {
            echo $row["name"] . "\\";
            echo $row["latitude"] . "\\";
            echo $row["longitude"] . "\\";
            echo $row["type"] . "\\";
            echo ";";
         }
      }
	  $result->close();
   }

   mysqli_close($con);
?>
