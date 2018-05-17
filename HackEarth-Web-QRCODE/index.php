<?php
session_start();
require_once("dbcontroller.php");
$db_handle = new DBController();
$mobile_image = "";
$mobile_done  = 0;

if(!empty($_GET["action"])) {
switch($_GET["action"]) {
	case "add":
		$mobile_done = 0;	
		if(!empty($_POST["quantity"])) {
			$productByCode = $db_handle->runQuery("SELECT * FROM tblproduct WHERE code='" . $_GET["code"] . "'");
			$itemArray = array($productByCode[0]["code"]=>array('name'=>$productByCode[0]["name"], 'code'=>$productByCode[0]["code"], 'quantity'=>$_POST["quantity"], 'price'=>$productByCode[0]["price"]));
			
			if(!empty($_SESSION["cart_item"])) {
				if(in_array($productByCode[0]["code"],$_SESSION["cart_item"])) {
					foreach($_SESSION["cart_item"] as $k => $v) {
							if($productByCode[0]["code"] == $k)
								$_SESSION["cart_item"][$k]["quantity"] = $_POST["quantity"];
					}
				} else {
					$_SESSION["cart_item"] = array_merge($_SESSION["cart_item"],$itemArray);
				}
			} else {
				$_SESSION["cart_item"] = $itemArray;
			}
		}
	break;
	case "remove":
		$mobile_done = 0;
		if(!empty($_SESSION["cart_item"])) {
			foreach($_SESSION["cart_item"] as $k => $v) {
					if($_GET["code"] == $k)
						unset($_SESSION["cart_item"][$k]);				
					if(empty($_SESSION["cart_item"]))
						unset($_SESSION["cart_item"]);
			}
		}
	break;
	case "empty":
		$mobile_done = 0;
		unset($_SESSION["cart_item"]);
	break;	

	case "mobile":
		$total_amount = $_POST["total"];
		$payment_id = $db_handle->createPayment($total_amount);
		include "panam/qrlib.php";  
		QRcode::png($payment_id."|".$total_amount, $total_amount.'.png'); // creates file 			
		$mobile_image = $total_amount.'.png';
		$mobile_done  = 1;
	break;	
}
}
?>
<HTML>
<HEAD>
<TITLE>Mobile Payment Cart</TITLE>
<link href="style.css" type="text/css" rel="stylesheet" />
<script language="JavaScript">

	var payment_id = 0;
	var stop = 0;

	function setPayment(pay_id)
	{
		payment_id = pay_id;
		console.log("Payment" + pay_id);
	}

    function mac()
	{
		if(payment_id > 0)
		{
			console.log("Payment is set...checking whether payment done ?");
			var xmlhttp = new XMLHttpRequest();
			xmlhttp.onreadystatechange = function()
			{
				xmlhttp.responseText;
				if(xmlhttp.responseText == 1)
				{
					document.getElementById("qrcode").src="tick.png";
					document.getElementById("thanks").innerHTML = "Payment received ! Thank you !";
					console.log("Payment Done !!!");
					payment_id=0;
				}

			};
			xmlhttp.open("GET", "checkPayment.php?payment_id="+payment_id, true);
			xmlhttp.send();
		}

    }
    mac();
    setInterval(mac, 5000);
</script>
</HEAD>
<BODY>
<div id="shopping-cart">
<div class="txt-heading">Shopping Cart <a id="btnEmpty" href="index.php?action=empty">Empty Cart</a></div>
<?php
if(isset($_SESSION["cart_item"])){
    $item_total = 0;
?>	
<table cellpadding="10" cellspacing="1">
<tbody>
<tr>
<th><strong>Name</strong></th>
<th><strong>Code</strong></th>
<th><strong>Quantity</strong></th>
<th><strong>Price</strong></th>
<th><strong>Action</strong></th>
</tr>	
<?php		
    foreach ($_SESSION["cart_item"] as $item){
		?>
				<tr>
				<td><strong><?php echo $item["name"]; ?></strong></td>
				<td><?php echo $item["code"]; ?></td>
				<td><?php echo $item["quantity"]; ?></td>
				<td align=right><?php echo "&#8377;  ".$item["price"]; ?></td>
				<td><a href="index.php?action=remove&code=<?php echo $item["code"]; ?>" class="btnRemoveAction">Remove Item</a></td>
				</tr>
				<?php
        $item_total += ($item["price"]*$item["quantity"]);
		}
		?>

<tr>
	<td colspan="5" align=right><strong>Total:</strong> <?php echo "&#8377;  ".$item_total; ?></td>
</tr>
<tr>
	<td colspan="5" align=right>
		<?php
		if ($mobile_done == 1)
		{
			echo "<p id='thanks' class='btnRemoveAction'></p><img id='qrcode' src=".$mobile_image.">";
			//Call javascript here 
			echo '<script type="text/javascript">'
			   ,	'setPayment('.$payment_id.');'
			   , '</script>'
			   ;
		}
		else
		{
		?>
			<form method="post" action="index.php?action=mobile">
				<input type="hidden" name="total" value="<?php echo $item_total; ?>">
				<input type="submit" value="Pay by Mobile" class="btnAddAction" />
			</form>
		<?php
		}
		?>

	</td>
</tr>
</tbody>
</table>		
  <?php
}
?>
</div>

<div id="product-grid">
	<div class="txt-heading">Products</div>
	<?php
	$product_array = $db_handle->runQuery("SELECT * FROM tblproduct ORDER BY id ASC");
	if (!empty($product_array)) { 
		foreach($product_array as $key=>$value){
	?>
		<div class="product-item">
			<form method="post" action="index.php?action=add&code=<?php echo $product_array[$key]["code"]; ?>">
			<div class="product-image"><img src="<?php echo $product_array[$key]["image"]; ?>"></div>
			<div><strong><?php echo $product_array[$key]["name"]; ?></strong></div>
			<div class="product-price"><?php echo "&#8377;  ".$product_array[$key]["price"]; ?></div>
			<div><input type="text" name="quantity" value="1" size="2" /><input type="submit" value="Add to cart" class="btnAddAction" /></div>
			</form>
		</div>
	<?php
			}
	}
	?>
</div>
</BODY>
</HTML>