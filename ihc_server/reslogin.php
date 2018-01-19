<?php
   $dbhost="oniddb.cws.oregonstate.edu";
   $dbname="habibelo-db";
   $dbuser="habibelo-db";
   $dbpass="RcAbWdWDkpj7XNTL";
   $conn=mysqli_connect($dbhost,$dbuser,$dbpass,$dbname);

   if (mysqli_connect_errno($con)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }

   $email = "";
   $password = "";

   $email = $_POST['email'];
   $password = $_POST['password'];
   $result = mysqli_query($conn,"SELECT firstname, lastname FROM ihc_users where email='$email'and password='$password'");
   $row = mysqli_fetch_array($result);

   if($row[0]) {
      $data = json_encode($row[0]);
      echo $data;
   }
   mysqli_close($con);
?>
