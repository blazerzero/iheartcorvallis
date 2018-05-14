<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $stmt = $mysqli->prepare("SELECT * FROM ihc_survey");
  $stmt->execute();
  $result = $stmt->get_result();
  $questions = array();
  while ($question = $result->fetch_assoc()) {
    $questions[] = $question;
  }

  $words = $wordCounts = array();
  $stmt = $mysqli->prepare("SELECT comment FROM ihc_feedback");
  $stmt->execute();
  $result = $stmt->get_result();
  while ($row = $result->fetch_assoc()) {
    $comment = $row['comment'];
    $token = strtok($comment, ",. ");
    while ($token !== false) {
      if (strlen($token) > 1) {
        if (!array_key_exists($token, $wordCounts)) {
          $wordCounts[$token] = 1;
        }
        else {
          $wordCounts[$token]++;
        }
      }
      $token = strtok(",. ");
    }
  }
  arsort($wordCounts);
  $words = array_keys($wordCounts);

  ?>

  <html>
  <head>
    <title>Analysis Center - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script src= "./zingchart_branded_version/zingchart.min.js"></script>
    <script>
    zingchart.MODULESDIR = "./zingchart_branded_version/modules/";
    </script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script>
    function validateRegressionForm() {
      var stat1Field = document.forms["regressionForm"]["stat1"].value;
      var stat2Field = document.forms["regressionForm"]["stat2"].value;
      if (stat1Field == null || stat1Field == "" ||
          stat2Field == null || stat2Field == "") {
        alert("Please choose two statistics to compare!");
        return false;
      }
      else {
        return true;
      }
    }

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
      var myConfig = {
        "graphset":[
          {
            "type":"wordcloud",
            "options":{
              "style":{
                fontFamily:'Tahoma',
                "tooltip":{
                  visible: true,
                  text: '%text: %hits'
                }
              },
              "words":[
                <?php for ($i = 0; $i < count($words) - 1; $i++) { ?>
                  {
                    "text":"<?php echo $words[$i]; ?>",
                    "count":"<?php echo $wordCounts[$words[$i]]; ?>"
                  },
                <?php } ?>
                {
                  "text":"<?php echo $words[count($words)-1]; ?>",
                  "count":"<?php echo $wordCounts[$words[count($words)-1]]; ?>"
                }
              ]
            }
          }
        ]
      };
      zingchart.render({
        id: 'comment_word_cloud',
        data: myConfig,
        height: '100%',
        width: '100%'
      });
      zingchart.node_click = function(p) {
        zcdocs.demos.dump('node_click', p);
      }
      zingchart.bind('comment_word_cloud', 'node_click', function(p) {
        alert("node index: " + p.nodeindex);
        switch (p.nodeIndex) {
          default: {
            var link = './analyze_word.occurrences.php?word=this';
            window.open(link, '_blank');
          }
        }
      });
    });
    </script>
  </head>
  <body>
    <div class="siteheader" id="siteheader"></div>

    <div class="mainbody">
      <left class="sectionheader"><h1>Analysis Center</h1></left><br>
      <div class="ui divider"></div><br>
      <div>
        <h2>Find a Correlation</h2>
        <?php $j = 0; ?>
        <form name="regressionForm" onsubmit="return validateRegressionForm()" action="./get_correlation_data.php" method="post" enctype="multipart/form-data">
          <span style="font-size: 1.25vw"><strong>Compare</strong></span><br><br>
          <select class="ui search dropdown" name="stat1" id="stat1">
            <option value="">Choose statistic</option>
            <option value="1">Change in Time</option>
            <option value="2">Grade</option>
            <option value="3">User Type</option>
            <option value="4">Number of Completed Events</option>
          </select><br><br>
          <span style="font-size: 1.25vw"><strong>to</strong></span><br><br>
          <select class="ui search dropdown" name="stat2" id="stat2">
            <option value="">Choose statistic</option>
            <option value="5">Number of Completed Events</option>
            <?php for ($j = 1; $j <= count($questions); $j++) { ?>
              <option value="<?php echo $j+5; ?>">Responses to Survey Question: <?php echo $questions[$j-1]['question']; ?></option>
            <?php } ?>
          </select><br><br>
          <input class="ui green button" type="submit" value="Create Regression Visual">
        </form><br>
      </div><br>

      <div class="ui divider"></div><br>

      <div>
        <h2>App Comments: Word Cloud</h2>
        <p class="test"></p>
        <table>
          <tr>
            <td>
              <div id="comment_word_cloud" style="width: 50vw; height: 50vw;"></div>
            </td>
            <td>
              <table class="ui celled padded table" style="width: 100%; height: 30vw; display: block; overflow-y:auto; overflow-x:hidden">
                <thead>
                  <tr>
                    <th class="single line">Word</th>
                    <th>Number of Occurrences</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  <?php for ($i = 0; $i < count($words); $i++) { ?>
                    <tr>
                      <td><?php echo $words[$i]; ?></td>
                      <td><?php echo $wordCounts[$words[$i]]; ?></td>
                      <td><a href="./analyze_word_occurrences.php?word=<?php echo $words[$i]; ?>&count=<?php echo $wordCounts[$words[$i]]; ?>" class="ui green button">Analyze Occurences</a></td>
                    </tr>
                  <?php } ?>
                </tbody>
              </table>
            </td>
          </tr>
        </table>
      </div>

      <div class="ui divider"></div><br>

      <div>
        <h2>Keyword Search: App Comments</h2>
        <form name="keywordForm" onsubmit="return validateKeywordForm()" action="./get_keyword_comments.php" method="post" enctype="multipart/form-data">
          <span style="font-size: 1.25vw"><strong>Enter Keyword or Phrase: </strong></span>
          <input class="inputbox" type="text" name="keyword">
          <input class="ui green button" type="submit" value="Search">
        </form>
      </div>

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
