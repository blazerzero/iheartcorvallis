<?php
require 'db.php';
$id = $_GET['id'];
$sql = 'DELETE FROM ihc_events WHERE eventid=:id';
$statement = $connection->prepare($sql);
if ($statement->execute([':id' => $id])) {
  header("Location: /");
}