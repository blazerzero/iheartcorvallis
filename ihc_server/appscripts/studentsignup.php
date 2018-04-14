<?php
  $dbhost="oniddb.cws.oregonstate.edu";
  $dbname="habibelo-db";
  $dbuser="habibelo-db";
  $dbpass="RcAbWdWDkpj7XNTL";

  $mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);

	if ($mysqli->connect_error) {
		die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
	}

  $curPageURL = "http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/studentsignup.php";

  if ($_SERVER["REQUEST_METHOD"] == "POST") {
    /*if (!isset($_POST["ticket"])) {
      header("Location: https://login.oregonstate.edu/idp/profile/cas/login?service=" . $curPageURL);
    }
    else if (isset($_POST["ticket"])) {
      $ticket = $_POST["ticket"];
      $validatorURL = "https://login.oregonstate.edu/idp/profile/cas/serviceValidate?ticket=" . $ticket . "&service=" . $curPageURL;
      $xml = file_get_contents($validatorURL);
      $pattern = '/\\<cas\\:user\\>([a-zA-Z0-9]+)\\<\\/cas\\:user\\>/';
      preg_match($pattern, $xml, $matches);
      if ($matches && count($matches) > 1) {
        echo "Omeed\\";
        echo "Habibelahian\\";
        echo "8\\";
        echo "SIGNUPSUCCESS";
      }
      else {
        echo "SIGNUPERROR";
      }

    }*/

    //while (!isset($_POST['ticket'])) {
    //}

    $ticket = $_POST['ticket'];
    echo $ticket . "<br>";
    $validatorURL = "https://login.oregonstate.edu/idp/profile/cas/serviceValidate?ticket=" . $ticket . "&service=" . $curPageURL;
    $xml = file_get_contents($validatorURL);
    $pattern = '/\\<cas\\:user\\>([a-zA-Z0-9]+)\\<\\/cas\\:user\\>/';
    preg_match($pattern, $xml, $matches);
    if ($matches && count($matches) > 1) {
      echo "Omeed\\";
      echo "Habibelahian\\";
      echo "8\\";
      echo "SIGNUPSUCCESS";
    }
    else {
      echo "SIGNUPERROR";
    }

  }
?>
