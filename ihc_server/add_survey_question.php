<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

   <!-- HTML GOES HERE -->

<?php }
else {
   $url = "./admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>
