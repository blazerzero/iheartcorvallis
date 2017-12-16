<?php
include "./onidlogin.php";
include "./reslogin.php";
if (checkStudentAuth() != "") {
   session_start();
   unset($_SESSION);
   echo "unset DONE.\n";
   session_destroy();
   echo "session destroy DONE.\n";
}
sleep(3);
header("Location: index.html");
exit;
?>
