<!DOCTYPE HTML>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $stmt = $mysqli->prepare("SELECT * FROM ihc_resource_info");
  $stmt->execute();
  $result = $stmt->get_result();
  $ihc_resources = array();
  while ($resource = $result->fetch_assoc()) {
    $ihc_resources[] = $resource;
  }
  ?>

  <html>
  <head>
    <title>Manage Resource Page Content - I Heart Corvallis Administrative Suite</title>
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
      <left class="sectionheader"><h1>Manage Resource Page Content</h1></left><br>
      <div class="ui divider"></div><br>

      <table class="ui celled padded table">
        <thead>
          <tr>
            <th class="single line">Title</th>
            <th>Description</th>
            <th>Link</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <?php foreach($ihc_resources as $resource): ?>
            <tr>
              <td><?php echo $resource['title']; ?></td>
              <td><?php echo $resource['description']; ?></td>
              <td><?php echo $resource['link']; ?></td>
              <td>
                <a href="edit_primary_resource.php?id=<?php echo $resource['id'] ?>" class="ui blue button">Edit</a>
                <a onclick="return confirm('Are you sure you want to delete this resource?')" href="./admin_server/delete_primary_resource.php?id=<?php echo $resource['id'] ?>&image=<?php echo $resource['image'] ?>" class='ui red button'>Delete</a>
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
else {
  $url = "./admin_auth.php";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>
