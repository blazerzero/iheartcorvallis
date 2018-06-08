<!DOCTYPE HTML>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>   <!-- the user is logged in -->

  <?php
  require './admin_server/db.php';
  $stmt = $mysqli->prepare("SELECT * FROM ihc_events");   // retrieve the information on every event
  $stmt->execute();
  $result = $stmt->get_result();
  $ihc_events = array();
  while ($event = $result->fetch_assoc()) {
    $ihc_events[] = $event;
  }
  ?>

  <html>
  <head>
    <title>Manage Events - I Heart Corvallis Administrative Suite</title>
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
      <left class="sectionheader"><h1>Manage Events</h1></left><br>
      <div class="ui divider"></div><br>

      <table class="ui celled padded table">
        <thead>
          <tr>
            <th class="single line">Name</th>   <!-- Event name -->
            <th>Host</th>   <!-- Event host -->
            <th>Location</th>   <!-- Event location -->
            <th>Date and Time</th>    <!-- Event Date/Time range -->
            <th>Event PIN</th>    <!-- Event Check-In PIN -->
            <th>Action</th>   <!-- View Event Summary, edit the event, or delete the event -->
          </tr>
        </thead>
        <tbody>
          <?php foreach($ihc_events as $event): ?>
            <tr>
              <td><?php echo $event['name']; ?></td>
              <td><?php echo $event['host']; ?></td>
              <td><?php echo $event['location']; ?></td>
              <td>
                <?php
                if ($event['startdt'] == '1900-01-01 00:00:00' && $event['enddt'] == '2099-12-31 23:59:59') {
                  echo 'Anytime';
                }
                else {
                  $startdt = date('M d, Y g:i A', strtotime($event['startdt']));
                  $enddt = date('M d, Y g:i A', strtotime($event['enddt']));
                  echo $startdt . " - " . $enddt;
                }
                ?>
              </td>
              <td><?php echo $event['pin']; ?></td>
              <td>
                <a href="summarize_event.php?eventid=<?php echo $event['eventid'] ?>" class="ui green button">View Summary</a>
                <a href="edit_event.php?eventid=<?php echo $event['eventid'] ?>" class="ui blue button">Edit</a>
                <a onclick="return confirm('Are you sure you want to delete this event?')" href="./admin_server/delete_event.php?eventid=<?php echo $event['eventid'] ?>&image=<?php echo $event['image'] ?>" class='ui red button'>Delete</a>
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
