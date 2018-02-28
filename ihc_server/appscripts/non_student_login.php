<?php
   $dbhost="oniddb.cws.oregonstate.edu";
   $dbname="habibelo-db";
   $dbuser="habibelo-db";
   $dbpass="RcAbWdWDkpj7XNTL";

   $isAuth = False;

   $mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
   //Output any connection error
   if ($mysqli->connect_error) {
       die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
   }

   $email = $row = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $email = $_POST['email'];
	  $stmt = mysqli->prepare("SELECT email FROM ihc_users WHERE email= ?");
	  $stmt->bind_param('s', $email);
	  $stmt->execute();
	  $result = $stmt->get_result();
      //$result = $mysqli->query("SELECT email FROM ihc_users WHERE email='$email'");
      
	  
      if ($result->num_rows > 0) {
		 $stmt2 = mysqli->prepare("SELECT firstname, lastname, email, id, stampcount FROM ihc_users WHERE email= ?");
		 $stmt2->bind_param('s','$email');
		 $stmt2->execute();
		 $res = $stmt2->get_result();
		 $row = $res->fetch_assoc();
		 $data = json_encode($row);
         //$result = $mysqli->query("SELECT firstname, lastname, email, id, stampcount FROM ihc_users WHERE email='$email'");
         //$row = $result->fetch_assoc();
         //$data = json_encode($row);
         echo $data;
		 $stmt2->close();
      }
      else {
         echo "AUTHERROR"; # unable to authenticate email/password
      }
	  $stmt->close();
   }
   mysqli_close($con);
?>
