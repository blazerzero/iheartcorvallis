<?php
include "./onidlogin.php";
include "./reslogin.php";
if (checkStudentAuth() != "") {
   session_start();
   unset($_SESSION);
   unset($_REQUEST);
   unset($_SERVER);
   $ticket = "";
   session_destroy();
}
header("Location: index.html");
exit;
?>


<?php
/*include "./onidlogin.php";
include "./reslogin.php";
if (checkStudentAuth() != "") {
   $_SESSION = array();
   if (ini_get("session.use_cookies")) {
      $params = session_get_cookie_params();
      setcookie(session_name(), '', time() - 42000,
         $params["path"], $params["domain"],
         $params["secure"], $params["httponly"]
      );
   }
   session_destroy();
}
header("Location: index.html");
exit;*/
?>
