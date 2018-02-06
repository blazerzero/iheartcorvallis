<?php
require 'db.php';
$message = '';
if (isset ($_POST['name'])  && isset($_POST['location']) ) {
  $name = $_POST['name'];
  $location = $_POST['location'];
  $sql = 'INSERT INTO ihc_events(name, location) VALUES(:name, :location)';
  $statement = $connection->prepare($sql);
  if ($statement->execute([':name' => $name, ':location' => $location])) {
    $message = 'data inserted successfully';
  }



}


 ?>
<?php require 'header.php'; ?>
<div class="container">
  <div class="card mt-5">
    <div class="card-header">
      <h2>Create a events</h2>
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
          <input type="text" name="name" id="name" class="form-control">
        </div>
        <div class="form-group">
          <label for="location">location</label>
          <input type="location" name="location" id="location" class="form-control">
        </div>
        <div class="form-group">
          <button type="submit" class="btn btn-info">Create a person</button>
        </div>
      </form>
    </div>
  </div>
</div>
<?php require 'footer.php'; ?>
