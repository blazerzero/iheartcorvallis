<?php

/*******************************************/
/* THIS IS THE SIGNUP SCRIPT FOR ALL USERS */
/*******************************************/

ini_set('display_errors', 1);
error_reporting(E_ALL);
ini_set('memory_limit', '1G');

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

	/* GET VALUES VIA POST */
	$firstname = $_POST["firstname"];
	$lastname = $_POST["lastname"];
	$email = $_POST["email"];
	$password = $_POST["password"];

	/* CHECK IF ACCOUNT ALREADY EXISTS */
	$stmt1 = $mysqli->prepare("SELECT * FROM ihc_users WHERE email=?");
	$stmt1->bind_param('s', $email);
	$stmt1->execute();
	$result = $stmt1->get_result();

	if ($result->num_rows > 0) {		// the account already exists
		$alreadyExists = 1;
	}
	else if ($result->num_rows == 0) {		// the account does not already exist

		/* ADD THE NEW ACCOUNT TO THE DATABASE */
		$stmt2 = $mysqli->prepare("INSERT INTO ihc_users (firstname, lastname, email, password) VALUES (?, ?, ?, ?)");
		$stmt2->bind_param('ssss', $firstname, $lastname, $email, $password);
		$stmt2->execute();
		if ($stmt2->error == "") {		// successfully added the new account to the database

			/* GET THE NEWLY ADDED ACCOUNT INFORMATION */
			$stmt3 = $mysqli->prepare("SELECT * FROM ihc_users WHERE email='$email'");
			$stmt3->bind_param('s', $email);
			$stmt3->execute();
			$res2 = $stmt3->get_result();
			if ($res2->num_rows > 0) {
				$user = $res2->fetch_assoc();

				/* SEND THE USER'S INFORMATION TO THE APP */
				$id = $user["id"];
				echo $firstname . " " . $lastname . "\\";
				echo $email . "\\";
				echo $id . "\\";
				echo "SIGNUPSUCCESS";		// send success message to the app
			}
			$stmt3->close();
		}
		else {		// error adding the new account to the database
			echo "SIGNUPERROR";		// send error message to the app
		}
		$stmt2->close();
	}
	else {		// there are multiple accounts connected to this email
		echo "DUPACCOUNTERROR";		// send error message to the app
	}
	$stmt1->close();
}

$mysqli->close();
?>
