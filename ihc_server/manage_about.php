<!DOCTYPE HTML>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>   <!-- the user is logged in -->

  <?php
  require './admin_server/db.php';
  $stmt = $mysqli->prepare("SELECT * FROM ihc_about");    // retrieve information about the About Page
  $stmt->execute();
  $result = $stmt->get_result();
  $ihc_about = array();
  while ($about = $result->fetch_assoc()) {
    $ihc_about[] = $about;
  }
  ?>

  <html>
  <head>
    <title>Manage About Page - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script>
    $(document).ready(function() {
      $("#siteheader").load("siteheader.html");   // load the site header and navigation bar
    });
    </script>
  </head>
  <body>
    <div class="siteheader" id="siteheader"></div>

    <div class="mainbody">
      <left class="sectionheader"><h1>Manage About Page</h1></left><br>
      <div class="ui divider"></div><br>

      <table class="ui celled padded table">
        <thead>
          <tr>
            <th class="single line">About Page</th>
            <th>Action</th>   <!-- edit the About Page contents -->
          </tr>
        </thead>
        <tbody>
          <?php foreach($ihc_about as $about): ?>
            <tr>
              <td><?php echo $about['info']; ?></td>
              <td>
                <a href="edit_about.php?id=<?php echo $about['id'] ?>" class="ui blue button">Edit</a>
              </td>
            </tr>
          <?php endforeach; ?>
        </tbody>
      </table>
    </div>
  </body>
  </html>

  <?php
  $mysqli->close();
}
else {    // the user is not logged in
  $url = "./admin_auth.php";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect the user to the login page
}
?>
