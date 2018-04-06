<?php
	$dbhost="oniddb.cws.oregonstate.edu";
	$dbname="habibelo-db";
	$dbuser="habibelo-db";
	$dbpass="RcAbWdWDkpj7XNTL";
	
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
		
		$result = $mysqli->query("INSERT INTO ihc_users (firstname, lastname, email, password) VALUES ('$firstname', '$lastname', '$email', '$password')");
		
		if ($result == True) {
			echo "SIGNUPSUCCESS";
		}
		else {
			echo "SIGNUPERROR";
		}
	}
	
	$mysqli->close();
?>
