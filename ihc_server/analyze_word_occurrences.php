<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $word = $_GET['word'];
  $wordCount = $_GET['count'];

  $keyword = "%" . $word . "%";
  $topFiveRatedAll = $bottomFiveRatedAll = $topFiveRatedStudent = $bottomFiveRatedStudent = array();

  $stmt = $mysqli->prepare("SELECT * FROM ihc_users U NATURAL JOIN ihc_feedback F WHERE U.id=F.userid AND F.comment LIKE ? ORDER BY F.rating DESC");
  $stmt->bind_param('s', $keyword);
  $stmt->execute();
  $res = $stmt->get_result();
  if ($res->num_rows > 0) {
    $i = 1;
    while ($row = $res->fetch_assoc()) {
      if ($i <= 5) {
        $topFiveRatedAll[] = $row;
      }
      if ($res->num_rows - $i < 5) {
        $bottomFiveRatedAll[] = $row;
      }
      $i++;
    }
  }

  $stmt = $mysqli->prepare("SELECT * FROM ihc_users U NATURAL JOIN ihc_feedback F WHERE U.id=F.userid AND U.type < 3 AND F.comment LIKE ? ORDER BY F.rating DESC");
  $stmt->bind_param('s', $keyword);
  $stmt->execute();
  $res = $stmt->get_result();
  if ($res->num_rows > 0) {
    $i = 1;
    while ($row = $res->fetch_assoc()) {
      if ($i <= 5) {
        $topFiveRatedStudent[] = $row;
      }
      if ($res->num_rows - $i < 5) {
        $bottomFiveRatedStudent[] = $row;
      }
      $i++;
    }
  }

  arsort($bottomFiveRatedAll);
  arsort($bottomFiveRatedStudent);

  $types = array('Domestic Student', 'International Student', 'Faculty', 'Resident', 'Visitor');
  $grades = array('N/A', 'Freshman', 'Sophomore', 'Junior', 'Senior', 'Graduate Student', 'Doctoral Student', 'Faculty');
  ?>

  <html>
  <head>
    <title>Analyze Word Occurences: <?php echo $word; ?> - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script>
    function validateKeywordForm() {
      var keywordField = document.forms["keywordForm"]["keyword"].value;
      if (keywordField == null || keywordField == "") {
        alert("Please enter a keyword or phrase!");
        return false;
      }
      else {
        return true;
      }
    }
    </script>
    <script>
    $(document).ready(function() {
      $("#siteheader").load("siteheader.html");
    });
    </script>
  </head>
  <body>
    <div class="siteheader" id="siteheader"></div>

    <div class="mainbody">
      <left class="sectionheader"><h1>Analysis Center</h1></left><br>
      <div class="ui divider"></div><br>
      <div>
        <h2>Analyze Word Occurrences: "<?php echo $word; ?>"</h2>
        <h4>Number of Occurrences: <?php echo $wordCount; ?></h4>
      </div><br><br>

      <div>
        <h2>Top Rated Comments: All Users</h2>
        <table class="ui celled padded table">
          <thead>
            <tr>
              <th class="single line">Name</th>
              <th>Date and Time</th>
              <th>User Type</th>
              <th>Rating</th>
              <th>Comment</th>
            </tr>
          </thead>
          <tbody>
            <?php foreach($topFiveRatedAll as $tuple) { ?>
              <tr>
                <td><?php echo $tuple['firstname'] . ' ' . $tuple['lastname']; ?></td>
                <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                <td>
                  <?php
                  echo $types[$tuple['type']];
                  ?>
                </td>
                <td><?php echo $tuple['rating']; ?></td>
                <td><?php echo $tuple['comment']; ?></td>
              </tr>
            <?php } ?>
          </tbody>
        </table>

        <h2>Lowest Rated Comments: All Users</h2>
        <table class="ui celled padded table">
          <thead>
            <tr>
              <th class="single line">Name</th>
              <th>Date and Time</th>
              <th>User Type</th>
              <th>Rating</th>
              <th>Comment</th>
            </tr>
          </thead>
          <tbody>
            <?php foreach($bottomFiveRatedAll as $tuple) { ?>
              <tr>
                <td><?php echo $tuple['firstname'] . ' ' . $tuple['lastname']; ?></td>
                <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                <td>
                  <?php
                  echo $types[$tuple['type']];
                  ?>
                </td>
                <td><?php echo $tuple['rating']; ?></td>
                <td><?php echo $tuple['comment']; ?></td>
              </tr>
            <?php } ?>
          </tbody>
        </table>
      </div><br>

      <div class="ui divider"></div><br>

      <div>
        <h2>Top Rated Comments: Students and Faculty</h2>
        <table class="ui celled padded table">
          <thead>
            <tr>
              <th class="single line">Name</th>
              <th>Date and Time</th>
              <th>User Type</th>
              <th>Rating</th>
              <th>Comment</th>
            </tr>
          </thead>
          <tbody>
            <?php foreach($topFiveRatedStudent as $tuple) { ?>
              <tr>
                <td><?php echo $tuple['firstname'] . ' ' . $tuple['lastname']; ?></td>
                <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                <td>
                  <?php
                  echo $types[$tuple['type']];
                  ?>
                </td>
                <td><?php echo $tuple['rating']; ?></td>
                <td><?php echo $tuple['comment']; ?></td>
              </tr>
            <?php } ?>
          </tbody>
        </table>

        <h2>Lowest Rated Comments: Students and Faculty</h2>
        <table class="ui celled padded table">
          <thead>
            <tr>
              <th class="single line">Name</th>
              <th>Date and Time</th>
              <th>User Type</th>
              <th>Rating</th>
              <th>Comment</th>
            </tr>
          </thead>
          <tbody>
            <?php foreach($bottomFiveRatedStudent as $tuple) { ?>
              <tr>
                <td><?php echo $tuple['firstname'] . ' ' . $tuple['lastname']; ?></td>
                <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                <td>
                  <?php
                  echo $types[$tuple['type']];
                  ?>
                </td>
                <td><?php echo $tuple['rating']; ?></td>
                <td><?php echo $tuple['comment']; ?></td>
              </tr>
            <?php } ?>
          </tbody>
        </table>
      </div><br><br>

      <form name="keywordForm" onsubmit="return validateKeywordForm()" action="./get_keyword_comments.php" method="post" enctype="multipart/form-data">
        <span style="font-size: 1.25vw; display: none"><strong>Enter Keyword or Phrase: </strong></span>
        <input class="inputbox" type="text" name="keyword" value="<?php echo $word; ?>" style="display: none">
        <input class="ui green button" type="submit" value="View All Occurrences">
      </form>

    </div>
  </body>
  </html>


  <?php
  $stmt->close();
  $mysqli->close();
}
else {
  $url = "./admin_auth.php";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>
