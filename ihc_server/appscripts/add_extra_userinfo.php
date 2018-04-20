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

   $id = $studentid = $onid = $grade = $type = $birthdate = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
     $id = $_POST['userid'];
     $studentid = $_POST['studentid'];
     $onid = $_POST['onid'];
     $grade = $_POST['grade'];
     $type = $_POST['type'];
     $birthdate = $_POST['birthdate'];

     $stmt = $mysqli->prepare("UPDATE ihc_users SET studentid=?, onid=?, grade=?, type=?, birthdate=? WHERE id=?");
     $stmt->bind_param('ssiisi', $studentid, $onid, $grade, $type, $birthdate, $id);
     $stmt->execute();
     $result = $stmt->get_result();
     if ($result == True) {
       echo "ADDSUCCESS";
     }
     else {
       echo "ADDERROR";
     }
     $result->close();
   }
   $mysqli->close();
?>
