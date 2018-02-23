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

   $firstname = $lastname = $email = $password = $row = "";
   $student = 0;

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $firstname = $_POST["firstname"];
      $lastname = $_POST["lastname"];
      $email = $_POST["email"];
      $password = $_POST["password"];

      # CHECK IF ACCOUNT ALREADY EXISTS
      $result = $mysqli->query("SELECT email FROM ihc_users WHERE email='$email'");
      if ($result->num_rows > 0) {
         $alreadyExists = True;
      }

      # ADD ACCOUNT IF IT DOESN'T ALREADY EXIST
      if ($alreadyExists == False) {
         $result = $mysqli->query("SELECT id FROM ihc_users");
         $id = $result->num_rows + 1;
		 
		 $stmt = $mysqli->prepare("INSERT INTO ihc_users (student, firstname, lastname, email, password, id) VALUES (?, ?, ?, ?, ?, ?)");
         $stmt->bind_param("sssssi", '$student', '$firstname', '$lastname', '$email', '$password', '$id');
		 
		 $stmt->execute();
		 
		// $result = $mysqli->query("INSERT INTO ihc_users (student, firstname, lastname, email, password, id) VALUES ('$student', '$firstname', '$lastname', '$email', '$password', '$id')");

         if ($stmt == True) {
            echo "SIGNUPSUCCESS"; # account successfully added to database
         }
         else {
            echo "SIGNUPERROR"; # error adding account to database
         }
		  $stmt->close();
      }
      else {
         echo "DUPACCOUNTERROR"; # account already exists
      }
	 
   }

   mysqli_close($con);
?>
