<?php

/*****************************************/
/* ADD A SURVEY RESPONSE TO THE DATABASE */
/*****************************************/

ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');

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

  /* GET VALUES VIA POST */
  $userid = $_POST['userid'];
  $dateandtime = date("Y-m-d H:i:s");
  $questionid = $_POST['questionid'];
  $response = $_POST['response'];

  /* ADD SURVEY RESPONSE TO DATABASE */
  $stmt1 = $mysqli->prepare("INSERT INTO ihc_survey_responses (userid, dateandtime, questionid, response) VALUES (?, ?, ?, ?)");
  $stmt1->bind_param('isis', $userid, $dateandtime, $questionid, $response);
  $stmt1->execute();
  if ($stmt1->error == "") {    // successfully added survey response to database

    /* INDICATE THAT THE USER HAS COMPLETED THE SURVEY */
    $stmt2 = $mysqli->prepare("UPDATE ihc_users SET didsurvey = 1 WHERE id=?");
    $stmt2->bind_param('s', $userid);
    $stmt2->execute();
    if ($stmt2->error == "") {    // successfully marked survey as complete for the user
      echo "ADDSUCCESS";    // send success message to the app
    }
    else {    // error marking the survey as complete for the user
      echo "ADDERROR";    // send error message to the app
    }
    $stmt2->close();
  }
  else {    // error adding survey response to the database
    echo "ADDERROR";    // send error message to the app
  }
  $stmt1->close();
}

$mysqli->close();
?>
