<?php

/********************************************************/
/* GET USERS' NAMES AND STAMPCOUNTS FOR THE LEADERBOARD */
/********************************************************/

ini_set('display_errors', 1);
error_reporting(E_ALL);
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

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET THE USERS' NAMES AND STAMPCOUNTS */
  $stmt = $mysqli->prepare("SELECT firstname, lastname, stampcount FROM ihc_users");
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {   // for each user
      if ($row["stampcount"] > 0) {   // if they've completed at least one event
        $data = json_encode($row);    // encode the user's name and stampcount into JSON
        echo $data;   // send the user's name and stampcount to the app
        echo "\\";    // add a delimiter to the user's data
      }
    }
  }
  $stmt->close();
}

$mysqli->close();
?>
