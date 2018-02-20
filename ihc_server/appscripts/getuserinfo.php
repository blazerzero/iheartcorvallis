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

   $email = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $email = $_POST['email'];
	  /*$stmt = $mysqli->prepare("SELECT firstname, lastname, email, id, stampcount FROM ihc_users WHERE email = ?");
	  $stmt->bind_param('$email');
	  $stmt->execute();
	  $result = $stmt->get_result();*/
      $result = $mysqli->query("SELECT firstname, lastname, email, id, stampcount FROM ihc_users WHERE email = '$email'");
      if ($result->num_rows > 0) {
         while ($row = $result->fetch_assoc()) {
            $data = json_encode($row);
            echo $data;
         }
      }
	  $result->close();
   }

   mysqli_close($con);
?>
