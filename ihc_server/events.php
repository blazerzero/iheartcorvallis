<!DOCTYPE HTML>
<html>
<head>
   <link type="text/css" rel="stylesheet" href="./Semantic-UI-CSS-master/semantic.css"/>
   <link type="text/css" rel="stylesheet" href="./stylesheet.css"/>
   <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
   <script>
   $(document).ready(function() {
      $("#pin_generator").click(function() {
         $("#pin_holder").val((Math.floor((Math.random() * 9000) + 1000)).toString());
      });
   });
   </script>
</head>
<body>
   <div class="siteheader">
      <br><br>
      <left class="sitenametop">I HEART CORVALLIS</left>
      <br><br>
      <left class="sitenamebottom">Administrative Suite</left>
      <br><br>
   </div>
   <ul class="navbar">
      <div class="ui pointing dropdown link item">
         <a class="text">Home</a>
      </div>
      <div class="ui pointing dropdown link item">
         <a class="text">Events</a>
         <div class="menu">
            <div class="item">Add an Event</div>
            <div class="item">Manage Events</div>
         </div>
      </div>
      <div class="ui pointing dropdown link item">
         <a class="text">Resources</a>
         <div class="menu">
            <div class="item">Manage Primary Resources</div>
            <div class="item">Manage Resource Map</div>
         </div>
      </div>
      <div class="ui pointing dropdown link item">
         <a class="text">Prizes</a>
         <div class="menu">
            <div class="item">Add a Prize</div>
            <div class="item">Manage Prizes</div>
            <div class="item">Manage Prize Thresholds</div>
         </div>
      </div>
   </ul>

   <div class="mainbody">
      <left class="sectionheader"><strong>Add an Event</strong></left>
      <br><br>
      <form action="client.php" method="post">
         <div class="elem">
            Name of Event: <input class="inputbox" type="text" name="name"><br><br>
         </div>
         <div class="elem">
            Name of Location: <input class="inputbox" type="text" name="location"><br><br>
         </div>
         <div class="elem">
            Street Address: <input class="inputbox" type="text" name="streetaddress">
         </div>
         <div class="elem">
            City: <input class="inputbox" type="text" name="city">
         </div>
         <div class="elem">
            State: <input class="inputbox" type="text" name="state">
         </div>
         <div class="elem">
            ZIP: <input class="inputbox" type="text" name="zip"><br><br>
         </div>
         <div class="elem">
            Date: <input class="inputbox" type="date" name="date"><br><br>
         </div>
         <div class="elem">
            Time: <input class="inputbox" type="time" name="time"><br><br>
         </div>
         <div class="elem">
            Description of Event: <input class="inputbox" type="text" name="description"><br><br>
         </div>
         <div class="elem">
            Cover Image: <input class="ui button" type="file" name="image"><br><br>
         </div>
         <div class="elem">
            Link 1: <input class="inputbox" type="text" name="link1"><br><br>
         </div>
         <div class="elem">
            Link 2: <input class="inputbox" type="text" name="link2"><br><br>
         </div>
         <div class="elem">
            Link 3: <input class="inputbox"type="text" name="link3"><br><br>
         </div>
         <div class="elem">
            Event PIN:
            <input class="inputbox" type="text" name="pin" id="pin_holder">
            <button class="ui button" id="pin_generator" type="button">Generate PIN</button><br><br>
         </div>
         <input class="ui button" type="submit">
      </form>
   </div>
</body>
</html>
