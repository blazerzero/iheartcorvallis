<!DOCTYPE HTML>
<html>
<head>
<link type="text/css" rel="stylesheet" href="./Semantic-UI-CSS-master/semantic.css"/>
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

   <h1>Add Events</h1>
   <form action="client.php" method="post">
      Name of Event: <input type="text" name="name"><br>
      Name of Location: <input type="text" name="location"><br>
      Date and Time: <input type="text" name="dateandtime"><br>
      Description of event: <input type="text" name="description"><br>
      Image <input type="file" name="image"><br>
      Link 1: <input type="text" name="link1"><br>
      Link 2: <input type="text" name="link2"><br>
      Link 3: <input type="text" name="link3"><br>
      <div class="wrapper">
         Event PIN: <input type="text" name="pin" id="pin_holder">
         <button id="pin_generator" type="button">Generate PIN</button><br>
      </div>
      <input type="submit">
   </form>
</body>
</html>
