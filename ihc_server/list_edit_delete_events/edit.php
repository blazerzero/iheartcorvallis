<?php
require 'db.php';
$id = $_GET['id'];
$sql = 'SELECT * FROM ihc_events WHERE eventid=:id';
$statement = $connection->prepare($sql);
$statement->execute([':id' => $id ]);
$events = $statement->fetch(PDO::FETCH_OBJ);
if (isset ($_POST['name'])  && isset($_POST['location']) ) {
  $name = $_POST['name'];
  $location = $_POST['location'];
  $sql = 'UPDATE ihc_events SET name=:name, location=:location WHERE eventid=:id';
  $statement = $connection->prepare($sql);
  if ($statement->execute([':name' => $name, ':location' => $location, ':id' => $id])) {
    header("Location: /");
  }



}
 ?>


<?php require 'header.php'; ?>
<div class="container">
  <div class="card mt-5">
    <div class="card-header">
      <h2>Update Events</h2>
    </div>
    <div class="card-body">
      <?php if(!empty($message)): ?>
        <div class="alert alert-success">
          <?= $message; ?>
        </div>
      <?php endif; ?>
      <form method="post">
        <div class="form-group">
          <label for="name">Name</label>
          <input value="<?= $events->name; ?>" type="text" name="name" id="name" class="form-control">
        </div>
        <div class="form-group">
          <label for="location">location</label>
          <input type="location" value="<?= $events->location; ?>" name="location" id="location" class="form-control">
        </div>
        <div class="form-group">
          <button type="submit" class="btn btn-info">Update events</button>
        </div>
      </form>
    </div>
  </div>
</div>
<?php require 'footer.php'; ?>
