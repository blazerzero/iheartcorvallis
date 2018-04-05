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

   $userid = $dateandtime = $questionid = $response = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $userid = $_POST['userid'];
      $dateandtime = date("Y-m-d H:i:s");
      /*for ($i = 1; $i <= count($_POST) / 2; $i++) {
         $questionid = $_POST["questionid" . "$i"];
         $response = $_POST["responseid" . "$i"];
         $stmt = $mysqli->prepare("INSERT INTO ihc_survey_responses (userid, dateandtime, questionid, response) VALUES (?, ?, ?, ?)");
   	   $stmt->bind_param('isis', $userid, $dateandtime, $questionid, $response);
   	   $stmt->execute();
   	   $result = $stmt->get_result();
      }*/

      $questionid = $_POST['questionid'];
      $dateandtime = date("Y-m-d H:i:s");
      $response = $_POST['response'];
	   $stmt = $mysqli->prepare("INSERT INTO ihc_survey_responses (userid, dateandtime, questionid, response) VALUES (?, ?, ?, ?)");
	   $stmt->bind_param('isis', $userid, $dateandtime, $questionid, $response);
	   $stmt->execute();
	   $result = $stmt->get_result();

      //if ($result) {
         $stmt = $mysqli->prepare("UPDATE ihc_users SET didsurvey = 1 WHERE id = ?");
         $stmt->bind_param('s', $userid);
         $stmt->execute();
         $res = $stmt->get_result();
         //if ($res) {
            echo "ADDSUCCESS";
         //}
         /*else {
            echo "TRACKERROR";
         }*/
         $res->close();
      /*}
      else {
         echo "ADDERROR";
      }*/
      //echo $result;
      $result->close();
   }

   $mysqli->close();
?>
