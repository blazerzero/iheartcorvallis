<?php
require 'db.php';
$sql = 'SELECT eventid, name, location FROM ihc_events ';
$statement = $connection->prepare($sql);
$statement->execute();
$ihc_events = $statement->fetch(PDO::FETCH_OBJ);
 ?>

<?php require 'header.php'; ?>
<div class="container">
  <div class="card mt-5">
    <div class="card-header">
      <h2>All ihc_events</h2>
    </div>
    <div class="card-body">
      <table class="table table-bordered">
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>location</th>
          <th>Action</th>
        </tr>
        <?php foreach($ihc_events as $events): ?>
          <tr>
            <td><?= $events->eventid; ?></td>
            <td><?= $events->name; ?></td>
            <td><?= $events->location; ?></td>
            <td>
              <a href="edit.php?id=<?= $events->id ?>" class="btn btn-info">Edit</a>
              <a onclick="return confirm('Are you sure you want to delete this entry?')" href="delete.php?id=<?= $events->id ?>" class='btn btn-danger'>Delete</a>
            </td>
          </tr>
        <?php endforeach; ?>
      </table>
    </div>
  </div>
</div>
<?php require 'footer.php'; ?>
