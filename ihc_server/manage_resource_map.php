<!DOCTYPE HTML>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>   <!-- the user is logged in -->

  <?php
  require './admin_server/db.php';
  $stmt = $mysqli->prepare("SELECT * FROM ihc_resources");    // retrieve the information on every resource map marker
  $stmt->execute();
  $result = $stmt->get_result();
  $marker = array();
  $markers = array();
  $sortedMarkers = array();
  while ($marker = $result->fetch_assoc()) {
    $markers[] = $marker;
  }

  $indexLeft = 0;
  $pointer = 0;
  $i = 0;
  $indexRight = 1;

  for ($i = 0; $i < count($markers); $i++) {
    if ($markers[$i]['type'] == "Activities and Entertainment") {
      $sortedMarkers[] = $markers[$i];
    }
  }
  for ($i = 0; $i < count($markers); $i++) {
    if ($markers[$i]['type'] == "Grocery Stores") {
      $sortedMarkers[] = $markers[$i];
    }
  }
  for ($i = 0; $i < count($markers); $i++) {
    if ($markers[$i]['type'] == "Restaurants") {
      $sortedMarkers[] = $markers[$i];
    }
  }
  for ($i = 0; $i < count($markers); $i++) {
    if ($markers[$i]['type'] == "Shopping") {
      $sortedMarkers[] = $markers[$i];
    }
  }
  for ($i = 0; $i < count($markers); $i++) {
    if ($markers[$i]['type'] == "City Offices") {
      $sortedMarkers[] = $markers[$i];
    }
  }
  for ($i = 0; $i < count($markers); $i++) {
    if ($markers[$i]['type'] == "OSU Campus") {
      $sortedMarkers[] = $markers[$i];
    }
  }

  ?>

  <html>
  <head>
    <title>Manage Resource Map - I Heart Corvallis Administrative Suite</title>
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
      <left class="sectionheader"><h1>Manage Resource Map</h1></left><br>
      <div class="ui divider"></div><br>

      <table class="ui celled padded table">
        <thead>
          <tr>
            <th class="single line">Name</th>   <!-- Resource name -->
            <th>Address</th>    <!-- Resource address -->
            <th>Type</th>   <!-- Resource type -->
            <th>Action</th>   <!-- Edit or delete the resource -->
          </tr>
        </thead>
        <tbody>
          <?php foreach($sortedMarkers as $marker): ?>
            <tr>
              <td><?php echo $marker['name']; ?></td>
              <td><?php echo $marker['address']; ?></td>
              <td><?php echo $marker['type']; ?></td>
              <td>
                <a href="edit_marker.php?id=<?php echo $marker['id'] ?>" class="ui blue button">Edit</a>
                <a onclick="return confirm('Are you sure you want to delete this resource from the map?')" href="./admin_server/delete_marker.php?id=<?php echo $marker['id'] ?>" class='ui red button'>Delete</a>
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
