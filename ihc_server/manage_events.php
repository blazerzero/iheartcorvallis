<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

<?php
require './admin_server/db.php';
$result = $mysqli->query("SELECT * FROM ihc_events");
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
                  <th class="single line">Name</th>
                  <th>Host</th>
                  <th>Location</th>
                  <th>Date and Time</th>
                  <th>Action</th>
               </tr>
            </thead>
            <tbody>
               <?php foreach($ihc_events as $event): ?>
                  <tr>
                     <td><?php echo $event['name']; ?></td>
                     <td><?php echo $event['host']; ?></td>
                     <td><?php echo $event['location']; ?></td>
                     <td><?php echo $event['startdt'] . " - " . $event['enddt']; ?></td>
                     <td>
                        <a href="summarize_event.php?eventid=<?php echo $event['eventid'] ?>" class="ui green button">View Summary</a>
                        <a href="edit_event.php?eventid=<?php echo $event['eventid'] ?>" class="ui blue button">Edit</a>
                        <a onclick="return confirm('Are you sure you want to delete this event?')" href="./admin_server/delete_event.php?eventid=<?php echo $event['eventid'] ?>" class='ui red button'>Delete</a>
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
