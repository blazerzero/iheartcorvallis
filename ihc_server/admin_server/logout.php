<?php

ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');

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
