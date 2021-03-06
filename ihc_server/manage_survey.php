<!DOCTYPE HTML>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>   <!-- the user is logged in -->

  <?php
  require './admin_server/db.php';
  $stmt = $mysqli->prepare("SELECT * FROM ihc_survey");   // retrieve the information on every survey question
  $stmt->execute();
  $result = $stmt->get_result();
  $ihc_questions = array();
  while ($question = $result->fetch_assoc()) {
    $ihc_questions[] = $question;
  }
  ?>

  <html>
  <head>
    <title>Manage Survey - I Heart Corvallis Administrative Suite</title>
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
      <left class="sectionheader"><h1>Manage Survey</h1></left><br>
      <div class="ui divider"></div><br>

      <table class="ui celled padded table">
        <thead>
          <tr>
            <th class="single line">Question</th>   <!-- Survey question -->
            <th>Action</th>   <!-- View question responses, edit the question, or delete the question -->
          </tr>
        </thead>
        <tbody>
          <?php foreach($ihc_questions as $question): ?>
            <tr>
              <td><?php echo $question['question']; ?></td>
              <td>
                <a href="view_question_responses.php?id=<?php echo $question['id']; ?>" class="ui green button">View Responses</a>
                <a href="edit_question.php?id=<?php echo $question['id']; ?>" class="ui blue button">Edit</a>
                <a onclick="return confirm('Are you sure you want to delete this survey question?')" href="./admin_server/delete_survey_question.php?id=<?php echo $question['id']; ?>" class='ui red button'>Delete</a>
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
