CREATE TABLE IF NOT EXISTS `tblproduct` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `code` varchar(255) NOT NULL,
  `image` text NOT NULL,
  `price` double(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_code` (`code`)
)

INSERT INTO `tblproduct` (`id`, `name`, `code`, `image`, `price`) VALUES
(4, 'Ladies Skirt', 'women', 'product-images/dress.jpg', 799.00),
(5, 'iPhone', 'smartphone', 'product-images/iphone.jpg', 23633.00),
(6, 'Perfume', 'perfume001', 'product-images/perfume.jpg', 2345.00);
