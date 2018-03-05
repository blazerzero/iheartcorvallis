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

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $email = $_POST['email'];
      //$result = $mysqli->query("SELECT password FROM ihc_users WHERE email='$email'");
	  $stmt = $mysqli->prepare("SELECT password FROM ihc_users WHERE email= ?");
	  $stmt->bind_param('s', $email);
	  $stmt->execute();
	  $result = $stmt->get_result();
	 if ($result->num_rows > 0) {
		 
         $row = $result->fetch_assoc();
         echo $row['password'];
		 
      }
      else {
         echo "NOACCOUNTERROR";
      }
	$result->close();
   }

   mysqli_close($con);
?>
