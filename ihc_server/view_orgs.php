<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

<?php
require './admin_server/db.php';
$result = $mysqli->query("SELECT * FROM ihc_events");
$ihc_orgs = array();
while ($org = $result->fetch_assoc()) {
   if (!in_array($org['host'], $ihc_orgs)) {
      $ihc_orgs[] = $org['host'];
   }
}
?>

<html>
   <head>
      <title>View Organizations - I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
      <script>
      $(document).ready(function() {
         $("#siteheader").load("siteheader.html");
      });
      </script>
   </head>
   <body>
      <div class="siteheader" id="siteheader"></div>

      <div class="mainbody">
         <left class="sectionheader"><h1>View Organizations</h1></left><br>
         <div class="ui divider"></div><br>

         <table class="ui celled padded table">
            <thead>
               <tr>
                  <th class="single line">Organization</th>
                  <th>Action</th>
               </tr>
            </thead>
            <tbody>
               <?php foreach($ihc_orgs as $org): ?>
                  <tr>
                     <td><?php echo $org; ?></td>
                     <td>
                        <a href="view_org_summary.php?host=<?php echo $org; ?>" class="ui green button">View Summary</a>
                     </td>
                  </tr>
               <?php endforeach; ?>
            </tbody>
         </table>
      </div>
   </body>
</html>

<?php }
else {
   $url = "./admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>