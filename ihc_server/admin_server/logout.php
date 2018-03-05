<?php
require "./login.php";

//session_start();
unset($_SESSION["id"]);

session_unset();
session_destroy();
session_write_close();

//$params = session_get_cookie_params();
//setcookie(session_name(), '', 0, '/');
header("Location: ../admin_auth.php");
?>
