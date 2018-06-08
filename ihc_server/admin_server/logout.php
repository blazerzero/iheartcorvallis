<?php

ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');

require "./login.php";

unset($_SESSION["id"]);   // unset the session ID

/* DESTROY THE SESSION */
session_unset();
session_destroy();
session_write_close();

header("Location: ../admin_auth.php");    // redirect user to administrative authorization (login) page
?>
