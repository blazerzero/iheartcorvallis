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

   $userid = $dateandtime = $response1 = $response2 = $response3 = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $userid = $_POST['userid'];
      $dateandtime = date("Y-m-d H:i:s");
      $response1 = $_POST['response1'];
      $response2 = $_POST['response2'];
      $response3 = $_POST['response3'];
	   $stmt = $mysqli->prepare("INSERT INTO ihc_survey_responses (userid, dateandtime, a1, a2, a3) VALUES (?, ?, ?, ?, ?)");
	   $stmt->bind_param('issss', $userid, $dateandtime, $response1, $response2, $response3);
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
