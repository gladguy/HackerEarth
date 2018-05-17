<?php
	//	checkPayment.php?payment_id=1001
	require_once("dbcontroller.php");
	$db_handle = new DBController();

	$payment_id = $_GET["payment_id"];

	$result = $db_handle->checkPayment($payment_id);
	echo $result;
?>


