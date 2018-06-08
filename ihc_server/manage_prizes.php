<!DOCTYPE HTML>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>   <!-- the user is logged in -->

  <?php
  require './admin_server/db.php';
  $stmt = $mysqli->prepare("SELECT * FROM ihc_prizes");   // retrieve the information on every prize
  $stmt->execute();
  $result = $stmt->get_result();
  $ihc_prizes = array();
  $temp = array();
  while ($prize = $result->fetch_assoc()) {
    $ihc_prizes[] = $prize;
  }

  $indexLeft = 0;
  $pointer = 0;
  $i = 0;
  $indexRight = 1;

  // INSERTION SORT TO SORT PRIZES IN DESCENDING ORDER FROM GOLD TO BRONZE
  for ($i = 0; $i < count($ihc_prizes)-1; $i++) {
    while ($indexLeft >= 0
    && (($ihc_prizes[$indexLeft]["level"] == "silver" && $ihc_prizes[$indexRight]["level"] == "gold")
    || ($ihc_prizes[$indexLeft]["level"] == "bronze" && $ihc_prizes[$indexRight]["level"] == "gold")
    || ($ihc_prizes[$indexLeft]["level"] == "bronze" && $ihc_prizes[$indexRight]["level"] == "silver"))) {
      $temp = $ihc_prizes[$indexLeft];
      $ihc_prizes[$indexLeft] = $ihc_prizes[$indexRight];
      $ihc_prizes[$indexRight] = $temp;
      $indexLeft--;
      $indexRight--;
    }
    $pointer++;
    $indexLeft = $pointer;
    $indexRight = $pointer + 1;
  }
  ?>

  <html>
  <head>
    <title>Manage Prizes - I Heart Corvallis Administrative Suite</title>
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
      <left class="sectionheader"><h1>Manage Prizes</h1></left><br>
      <div class="ui divider"></div><br>

      <table class="ui celled padded table">
        <thead>
          <tr>
            <th class="single line">Name</th>   <!-- Prize name -->
            <th>Level</th>    <!-- Prize level -->
            <th>Action</th>   <!-- Edit or delete the prize -->
          </tr>
        </thead>
        <tbody>
          <?php foreach($ihc_prizes as $prize): ?>
            <tr>
              <td><?php echo $prize['name']; ?></td>
              <td><?php echo $prize['level']; ?></td>
              <td>
                <a href="edit_prize.php?prizeid=<?php echo $prize['prizeid'] ?>" class="ui blue button">Edit</a>
                <a onclick="return confirm('Are you sure you want to delete this prize?')" href="./admin_server/delete_prize.php?prizeid=<?php echo $prize['prizeid'] ?>" class='ui red button'>Delete</a>
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
