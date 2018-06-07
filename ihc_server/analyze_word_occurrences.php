<!DOCTYPE HTML>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>   <!-- the user is logged in -->

  <?php
  require './admin_server/db.php';
  $word = $_GET['word'];

  $wordCount = 0;
  $keyword = "%" . $word . "%";
  $topFiveRatedAll = $bottomFiveRatedAll = $topFiveRatedStudent = $bottomFiveRatedStudent = $topFiveRatedNonStudent = $bottomFiveRatedNonStudent = array();

  /* Get the number of occurrences of the word */
  $stmt = $mysqli->prepare("SELECT comment FROM ihc_feedback WHERE comment LIKE ?");
  $stmt->bind_param('s', $keyword);
  $stmt->execute();
  $res = $stmt->get_result();
  while ($row = $res->fetch_assoc()) {
    $comment = $row['comment'];
    $token = strtok($comment, ",.;:/ ");
    while ($token !== false) {
      if ($token == $word) {
        $wordCount++;
      }
      $token = strtok(",.;:/ ");
    }
  }

  /* Get the 5 highest-rated comments with this word */
  $stmt = $mysqli->prepare("SELECT * FROM ihc_users U NATURAL JOIN ihc_feedback F WHERE U.id=F.userid AND F.comment LIKE ? AND F.rating > 0 ORDER BY F.rating DESC, F.dateandtime DESC LIMIT 5");
  $stmt->bind_param('s', $keyword);
  $stmt->execute();
  $res = $stmt->get_result();
  if ($res->num_rows > 0) {
    while ($row = $res->fetch_assoc()) {
      $topFiveRatedAll[] = $row;
    }
  }

  /* Get the 5 lowest-rated comments with this word */
  $stmt = $mysqli->prepare("SELECT * FROM ihc_users U NATURAL JOIN ihc_feedback F WHERE U.id=F.userid AND F.comment LIKE ? AND F.rating > 0 ORDER BY F.rating ASC, F.dateandtime DESC LIMIT 5");
  $stmt->bind_param('s', $keyword);
  $stmt->execute();
  $res = $stmt->get_result();
  if ($res->num_rows > 0) {
    while ($row = $res->fetch_assoc()) {
      $bottomFiveRatedAll[] = $row;
    }
  }

  /* Get the 5 highest-rated OSU-affiliated comments with this word */
  $stmt = $mysqli->prepare("SELECT * FROM ihc_users U NATURAL JOIN ihc_feedback F WHERE U.id=F.userid AND U.type < 3 AND F.comment LIKE ? AND F.rating > 0 ORDER BY F.rating DESC, F.dateandtime DESC LIMIT 5");
  $stmt->bind_param('s', $keyword);
  $stmt->execute();
  $res = $stmt->get_result();
  if ($res->num_rows > 0) {
    while ($row = $res->fetch_assoc()) {
      $topFiveRatedStudent[] = $row;
    }
  }

  /* Get the 5 lowest-rated OSU-affiliated comments with this word */
  $stmt = $mysqli->prepare("SELECT * FROM ihc_users U NATURAL JOIN ihc_feedback F WHERE U.id=F.userid AND U.type < 3 AND F.comment LIKE ? AND F.rating > 0 ORDER BY F.rating ASC, F.dateandtime DESC LIMIT 5");
  $stmt->bind_param('s', $keyword);
  $stmt->execute();
  $res = $stmt->get_result();
  if ($res->num_rows > 0) {
    while ($row = $res->fetch_assoc()) {
      $bottomFiveRatedStudent[] = $row;
    }
  }

  /* Get the 5 highest-rated non-OSU-affiliated comments with this word */
  $stmt = $mysqli->prepare("SELECT * FROM ihc_users U NATURAL JOIN ihc_feedback F WHERE U.id=F.userid AND U.type >= 3 AND F.comment LIKE ? AND F.rating > 0 ORDER BY F.rating DESC, F.dateandtime DESC LIMIT 5");
  $stmt->bind_param('s', $keyword);
  $stmt->execute();
  $res = $stmt->get_result();
  if ($res->num_rows > 0) {
    while ($row = $res->fetch_assoc()) {
      $topFiveRatedNonStudent[] = $row;
    }
  }

  /* Get the 5 lowest-rated non-OSU-affiliated comments with this word */
  $stmt = $mysqli->prepare("SELECT * FROM ihc_users U NATURAL JOIN ihc_feedback F WHERE U.id=F.userid AND U.type >= 3 AND F.comment LIKE ? AND F.rating > 0 ORDER BY F.rating ASC, F.dateandtime DESC LIMIT 5");
  $stmt->bind_param('s', $keyword);
  $stmt->execute();
  $res = $stmt->get_result();
  if ($res->num_rows > 0) {
    while ($row = $res->fetch_assoc()) {
      $bottomFiveRatedNonStudent[] = $row;
    }
  }

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
      if (keywordField == null || keywordField == "") {   // if any required field is empty
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
      $("#siteheader").load("siteheader.html");   // load the site header and the navigation bar
    });
    </script>
  </head>
  <body>
    <div class="siteheader" id="siteheader"></div>

    <div class="mainbody">
      <left class="sectionheader"><h1>Analysis Center</h1></left><br>
      <form action="./analyze.php">
        <button class="ui red button">
          <i class="arrow left icon"></i>
          Back to Selection Prompt
        </button>
      </form>
      <div class="ui divider"></div><br>
      <div>
        <h2>Analyze Word Occurrences: "<?php echo $word; ?>"</h2>
        <h4>Number of Occurrences: <?php echo $wordCount; ?></h4>
      </div><br><br>

      <!-- If this word occurs in the comments at all -->
      <?php if ($wordCount > 0) { ?>
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
                <th>Student ID #</th>
                <th>ONID Username</th>
                <th>Date and Time</th>
                <th>User Type</th>
                <th>Class Standing</th>
                <th>Rating</th>
                <th>Comment</th>
              </tr>
            </thead>
            <tbody>
              <?php foreach($topFiveRatedStudent as $tuple) { ?>
                <tr>
                  <td><?php echo $tuple['firstname'] . ' ' . $tuple['lastname']; ?></td>
                  <td><?php echo $tuple['studentid']; ?></td>
                  <td><?php echo $tuple['onid']; ?></td>
                  <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                  <td>
                    <?php
                    echo $types[$tuple['type']];
                    ?>
                  </td>
                  <td><?php echo $tuple['grade']; ?></td>
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
                <th>Student ID #</th>
                <th>ONID Username</th>
                <th>Date and Time</th>
                <th>User Type</th>
                <th>Class Standing</th>
                <th>Rating</th>
                <th>Comment</th>
              </tr>
            </thead>
            <tbody>
              <?php foreach($bottomFiveRatedStudent as $tuple) { ?>
                <tr>
                  <td><?php echo $tuple['firstname'] . ' ' . $tuple['lastname']; ?></td>
                  <td><?php echo $tuple['studentid']; ?></td>
                  <td><?php echo $tuple['onid']; ?></td>
                  <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                  <td>
                    <?php
                    echo $types[$tuple['type']];
                    ?>
                  </td>
                  <td><?php echo $tuple['grade']; ?></td>
                  <td><?php echo $tuple['rating']; ?></td>
                  <td><?php echo $tuple['comment']; ?></td>
                </tr>
              <?php } ?>
            </tbody>
          </table>
        </div><br>

        <div class="ui divider"></div><br>

        <div>
          <h2>Top Rated Comments: Non-Students</h2>
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
              <?php foreach($topFiveRatedNonStudent as $tuple) { ?>
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

          <h2>Lowest Rated Comments: Non-Students</h2>
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
              <?php foreach($bottomFiveRatedNonStudent as $tuple) { ?>
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

        <form name="keywordForm" onsubmit="return validateKeywordForm()" action="./get_keyword_comments.php" method="post" enctype="multipart/form-data">
          <input class="inputbox" type="text" name="keyword" value="<?php echo $word; ?>" style="display: none">
          <input class="ui green button" type="submit" value="View All Occurrences">
        </form>
      <?php } ?>

    </div>
  </body>
  </html>


  <?php
  $stmt->close();
  $mysqli->close();
}
else {    // the user is not logged in
  $url = "./admin_auth.php";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect the user to the login page
}
?>
