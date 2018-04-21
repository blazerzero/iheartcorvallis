<?php

//require "./login.php";

//if (isset($_SESSION["id"]) && $_SESSION["id"] != null) {
   $dbhost="oniddb.cws.oregonstate.edu";
   $dbname="habibelo-db";
   $dbuser="habibelo-db";
   $dbpass="RcAbWdWDkpj7XNTL";

   $alreadyExists = False;

   $mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
   //Output any connection error
   if ($mysqli->connect_error) {
       die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
       $message = "Unable to connect to the resource marker database!";
       echo "<script type='text/javascript'>alert('$message');</script>";
       $url = "../add_marker.php";
       echo "<script type='text/javascript'>document.location.href = '$url';</script>";
       exit;
   }

   $title = $description = $link = "";
   $resourceids = array();

   if ($_SERVER["REQUEST_METHOD"] == "POST") {

      /* GET VALUES VIA POST */
      $title = $_POST["title"];
      $description = $_POST["description"];
      $link = $_POST["link"];

      /*$result = $mysqli->query("SELECT id FROM ihc_resource_info");
      while ($row = $result->fetch_assoc()) {
         $resourceids[] = $row;
      }

      $newresourceid = $resourceids[count($resourceids)-1]['id'] + 1;*/

      /* ADD IMAGE TO IMAGES DIRECTORY */

      $errors= array();
      $file_name = $_FILES['image']['name'];
      $file_size = $_FILES['image']['size'];
      $file_tmp = $_FILES['image']['tmp_name'];
      $file_type = $_FILES['image']['type'];
      $file_ext=strtolower(end(explode('.',$_FILES['image']['name'])));

      $expensions= array("jpeg","jpg","png");

     $new_dir = "../images/resources/".$file_name;

      if(in_array($file_ext,$expensions)=== false){
         $errors[]="extension not allowed, please choose a JPEG or PNG file.";
      }

      if($file_size > 2097152) {
         $errors[]='File size must be excately 2 MB';
      }

      if(empty($errors)==true) {
         if(move_uploaded_file($file_tmp, $new_dir)) {
          echo "The file HAS BEEN UPLOADED";
       }
         //echo "Success";
      }else{
         print_r($errors);
      }

      /* ADD RESOURCE TO DATABASE */
      $stmt = $mysqli->prepare("INSERT INTO ihc_resource_info (title, description, image, link) VALUES (?, ?, ?, ?)");
      $stmt->bind_param('ssss', $title, $description, $image, $link);
      $stmt->execute();

      $url = "";

      if ($stmt->error == "") {
         $message = "Resource has been added to the resource page!";
         $url = "../index.php";
      }
      else {
         $message = "Error adding resource to the resource page!"; # error adding prize to database
         $url = "../add_primary_resource.php";
      }

      $stmt->close();
      $mysqli->close();
      echo "<script type='text/javascript'>alert('$message');</script>";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
      exit;

   }

   $mysqli->close();
/*}
else {
   $url = "../admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}*/

?>
