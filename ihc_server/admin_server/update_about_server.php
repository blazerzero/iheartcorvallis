<?php

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
  echo "Connection failed!<br>";
}

$id = $info = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET VALUES VIA POST */
  $id = $_POST["id"];
  $info = $_POST["info"];
  $changeimage = $_POST["changeimage"];
  $image = $_POST["image"];

  if ($changeimage == 0) {    // if the user does not want to change the About Page image

    /* UPDATE THE INFO FIELD OF THE ABOUT PAGE */
    $stmt = $mysqli->prepare("UPDATE ihc_about SET info=? WHERE id=?");
    $stmt->bind_param('si', $info, $id);
    $stmt->execute();

    if ($stmt->error == "") {   // successfully updated About Page content
      $message = "The About Page content has been updated!";
    }
    else {    // error updating About Page content
      echo $stmt->error . "<br>";
      $message = "Error updating the About Page content!";
    }

    $url = "../manage_about.php";

    $stmt->close();
    $mysqli->close();
    echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
    echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url
    exit;
  }
  else if ($changeimage == 1) {   // the user wants to change the About Page image

    /* GET THE NAME OF THE CURRENT ABOUT PAGE IMAGE */
    $stmt = $mysqli->prepare("SELECT image FROM ihc_about WHERE id=?");
    $stmt->bind_param('i', $id);
    $stmt->execute();
    $res = $stmt->get_result();
    $row = $res->fetch_assoc();

    $picture = $row['image'];
    $dir = "../images/about/".$picture;   // build About Page image path

    if (!is_writable($dir)) {
 	   echo $dir . ' is not writeable';   // unable to access the image path
    }

    /* CHECK IF THE IMAGE IS USED IN ANY OTHER ABOUT PAGE SECTIONS */
    $stmt = $mysqli->prepare("SELECT id FROM ihc_about WHERE image=?");
    $stmt->bind_param('s', $picture);
    $stmt->execute();
    $res = $stmt->get_result();

    if ($res->num_rows == 1) {    // the image is not used in any other About Page sections
      if(!unlink($dir)) {   // error deleting the image
        echo 'Error deleting ' . $picture;
      }
      else {    // successfully deleted the image
        echo ('Deleted ' . $picture);
      }
    }

    /* SAVE THE NEW IMAGE */
    $errors= array();
    $file_name = $_FILES['image']['name'];
    $file_size = $_FILES['image']['size'];
    $file_tmp = $_FILES['image']['tmp_name'];
    $file_type = $_FILES['image']['type'];
    $file_ext=strtolower(end(explode('.',$_FILES['image']['name'])));

    $expensions= array("jpeg","jpg","png");

    $new_dir = "../images/about/".$file_name;

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

    /* UPDATE THE INFO AND IMAGE FIELDS OF THE ABOUT PAGE */
    $stmt = $mysqli->prepare("UPDATE ihc_about SET info=?, image=? WHERE id=?");
    $stmt->bind_param('ssi', $info, $file_name, $id);
    $stmt->execute();

    if ($stmt->error == "") {   // successfully updated About Page content
      $message = "The About Page content has been updated!";
    }
    else {    // error updating About Page content
      echo $stmt->error . "<br>";
      $message = "Error updating the About Page content!";
    }

    $url = "../manage_about.php";

    $stmt->close();
    $mysqli->close();
    echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
    echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url
    exit;
  }

}

$mysqli->close();

?>
