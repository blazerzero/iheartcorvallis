<?php
	$dbhost="oniddb.cws.oregonstate.edu";
	$dbname="habibelo-db";
	$dbuser="habibelo-db";
	$dbpass="RcAbWdWDkpj7XNTL";

	$alreadyExists = 0;

	$mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);

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
      $result = $mysqli->query("SELECT * FROM ihc_users WHERE email='$email'");
      if ($result->num_rows > 0) {
         $alreadyExists = 1;
      }

		if ($alreadyExists == 0) {
			$res1 = $mysqli->query("INSERT INTO ihc_users (firstname, lastname, email, password) VALUES ('$firstname', '$lastname', '$email', '$password')");
			$res2 = $mysqli->query("SELECT * FROM ihc_users WHERE email='$email'");
			if ($res1 == True && $res2 == True) {
				$user = $res2->fetch_assoc();
				$id = $user["id"];
				echo $firstname . " " . $lastname . "\\";
				echo $email . "\\";
				echo $id . "\\";
				echo "SIGNUPSUCCESS";
			}
			else {
				echo "SIGNUPERROR";
			}
		}
		else {
			echo "DUPACCOUNTERROR";
		}
	}

	$mysqli->close();
?>
