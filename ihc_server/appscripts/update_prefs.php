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

   $userid = $firstname = $lastname = $email = $type = $grade = $birthdate = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $userid = $_POST["userid"];
      $firstname = $_POST["firstname"];
      $lastname = $_POST["lastname"];
      $email = $_POST["email"];
      $type = $_POST["type"];
      $grade = $_POST["grade"];
      $birthdate = $_POST["birthdate"];
      $stmt = $mysqli->prepare("UPDATE ihc_users SET firstname=?, lastname=?, email=?, grade=?, type=?, birthdate=? WHERE id=?");
      $stmt->bind_param('sssiisi', $firstname, $lastname, $email, $grade, $type, $birthdate, $userid);
      $stmt->execute();
      $result = $stmt->get_result();
      //$result = $mysqli->query("UPDATE ihc_users SET firstname='$firstname', lastname='$lastname', email='$email', grade='$grade', type='$type', birthdate='$birthdate' WHERE id='$id'");
      //if ($result == True) {
         echo "UPDATESUCCESS";
      /*}
      else {
         echo "UPDATEERROR";
      }*/
	   //$stmt->close();
   }

   $mysqli->close();
?>
