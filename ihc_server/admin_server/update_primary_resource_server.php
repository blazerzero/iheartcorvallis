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

$id = $title = $description = $link = $changeimage = $image = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET VALUES VIA POST */
  $id = $_POST["id"];
  $title = $_POST["title"];
  $description = $_POST["description"];
  $link = $_POST["link"];
  $changeimage = $_POST["changeimage"];
  $image = $_POST["image"];

  $file_name = "";

  if ($changeimage == 0) {    // the user does not want to change the resource image

    /* GET THE NAME OF THE CURRENT EVENT IMAGE */
    $stmt = $mysqli->prepare("SELECT image FROM ihc_resource_info WHERE id=?");
    $stmt->bind_param('i', $id);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
      $row = $result->fetch_assoc();
      $file_name = $row['image'];   // remember the current image name
    }
    else {    // error retrieving the resource image name
      $message = "Error updating resource!";
      $url = "../manage_primary_resources.php";

      $stmt->close();
      $mysqli->close();
      echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url
      exit;
    }
  }

  else if ($changeimage == 1) {   // the user wants to change the resource image

    /* GET THE NAME OF THE CURRENT RESOURCE IMAGE */
    $stmt = $mysqli->prepare("SELECT image FROM ihc_resource_info WHERE id=?");
    $stmt->bind_param('i', $id);
    $stmt->execute();
    $res = $stmt->get_result();
    $row = $res->fetch_assoc();

    $picture = $row['image'];
    $dir = "../images/resources/".$picture;   // build resource image path

    if (!is_writable($dir)) {   // unable to access resource image
      echo $dir . ' is not writeable';
    }

    /* CHECK IF ANY OTHER RESOURCES USE THIS IMAGE */
    $stmt = $mysqli->prepare("SELECT id FROM ihc_resource_info WHERE image=?");
    $stmt->bind_param('s', $picture);
    $stmt->execute();
    $res = $stmt->get_result();

    if ($res->num_rows == 1) {    // this is the only resource using this image
      if(!unlink($dir)) {   // error deleting resource image
        echo 'Error deleting ' . $picture;
      }
      else {    // successfully deleted resource image
        echo ('Deleted ' . $picture);
      }
    }

    /* STORE THE NEW RESOURCE IMAGE */
    $errors= array();
    $file_name = $_FILES['image']['name'];
    $file_size = $_FILES['image']['size'];
    $file_tmp = $_FILES['image']['tmp_name'];
    $file_type = $_FILES['image']['type'];
    $file_ext=strtolower(end(explode('.',$_FILES['image']['name'])));

    $expensions= array("jpeg","jpg","png");

    $new_dir = "../images/events/".$file_name;

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

  }

  /* UPDATE THE RESOURCE INFORMATION */
  $stmt = $mysqli->prepare("UPDATE ihc_resource_info SET title=?, description=?, link=?, image=? WHERE id=?");
  $stmt->bind_param('ssssi', $title, $description, $link, $file_name, $id);
  $stmt->execute();

  if ($stmt->error == "") {   // successfully updated resource
    $message = "Resource has been updated!";
  }
  else {    // error updating resource
    $message = "Error updating resource!";
  }
  $url = "../manage_primary_resources.php";

  $stmt->close();
  $mysqli->close();
  echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url
}

$mysqli->close();

?>
