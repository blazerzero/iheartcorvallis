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
	  $stmt = $mysqli->prepare("SELECT firstname, lastname, stampcount FROM ihc_users");
	  $stmt->execute();
	  $result = $stmt->get_result();
      //$result = $mysqli->query("SELECT firstname, lastname, stampcount FROM ihc_users");
      if ($result->num_rows > 0) {
         while ($row = $result->fetch_assoc()) {
            if ($row["stampcount"] > 0) {
               $data = json_encode($row);
               echo $data;
               echo "\\";
            }
         }
      }
	  $result->close();
   }

   mysqli_close($con);
?>
