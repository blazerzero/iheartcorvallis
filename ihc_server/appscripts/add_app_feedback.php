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

   $userid = $dateandtime = $rating = $comment = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $userid = $_POST['userid'];
      $dateandtime = date("Y-m-d H:i:s");
      $rating = $_POST['rating'];
      $comment = $_POST['comment'];
	   $stmt = $mysqli->prepare("INSERT INTO ihc_feedback (userid, dateandtime, rating, comment) VALUES (?, ?, ?, ?)");
	   $stmt->bind_param('isis', $userid, $dateandtime, $rating, $comment);
	   $stmt->execute();
	   $result = $stmt->get_result();

      //if ($result) {
         /*$stmt = $mysqli->prepare("UPDATE ihc_users SET didsurvey = 1 WHERE id = ?");
         $stmt->bind_param('s', $userid);
         $stmt->execute();
         $res = $stmt->get_result();*/
         //if ($res) {
            echo "ADDSUCCESS";
         //}
         /*else {
            echo "TRACKERROR";
         }*/
         //$res->close();
      /*}
      else {
         echo "ADDERROR";
      }*/
      //echo $result;
      $result->close();
   }

   $mysqli->close();
?>
