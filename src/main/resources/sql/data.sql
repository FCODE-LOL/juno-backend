SET foreign_key_checks = 0;
INSERT INTO `TYPE` VALUES (1,'Phụ kiện',1),(2,'Trang sức',1),(3,'Mắt kính',1),(4,'Vớ',1);
INSERT INTO `USER` VALUES (1,'1@gmail.com','$2a$10$QYfEnwjFomI.4IZLv1Aw1.WIfczkw6BzxCB9Yywi0.qy2lIeLphLm','0329609321','a',NULL,1,NULL,NULL,'2021-09-15 22:50:56','1','2021-09-15 22:50:56',1,NULL,0),(2,'test@gmail','$2a$10$QYfEnwjFomI.4IZLv1Aw1.WIfczkw6BzxCB9Yywi0.qy2lIeLphLm',NULL,NULL,NULL,1,NULL,NULL,'2021-09-18 06:20:46','2','2021-09-18 06:20:46',10,NULL,0),(3,'2@gmail.com','$2a$10$QYfEnwjFomI.4IZLv1Aw1.WIfczkw6BzxCB9Yywi0.qy2lIeLphLm',NULL,NULL,NULL,0,NULL,NULL,'2021-09-19 12:16:27','3','2021-09-19 12:16:27',21,NULL,0);
INSERT INTO `PRODUCT` VALUES ('A2E3RS','Bông tai S','linkimage1','description','TQ','Bạc ',2,13,210000.000,'2021-09-19 12:14:29',1,0),('A2E3RT','Mắt kính đen','linkimage1','description','TQ','Bạc',3,13,210000.000,'2021-09-19 11:52:42',1,0),('A2E3RV','Vòng cổ J','linkimage1','description','TQ','Bạc',2,240000.000,220000.000,'2021-09-19 12:14:29',1,0),('A2E3RZ','Khuyên tai Z','linkimage1','description','Nhật','Vàng ',2,750000.000,500000.000,'2021-09-19 12:14:29',1,0),('A4E3RT','Vớ R','linkimage1&linkimage2','description','TQ','Cotton ',4,160000.000,110000.000,'2021-09-19 12:14:56',1,0);
INSERT INTO `MODEL` (`PRODUCT_id`, `color_id`, `link_images`, `size`, `quantity`, `price`, `discount_price`, `is_disable`) VALUES ('A2E3RS', 'blue', 'link1', '1', '20', '200000.000', '120000.000', '0'),('A2E3RT', 'yellow', 'link2', '2', '20', '300000.000', '220000.000', '0'),('A2E3RV', 'blue', 'link1', '1', '20', '200000.000', '120000.000', '0');
INSERT INTO `BILL` (`USER_id`, `customer_name`, `phone`, `area_id`, `address`, `payment_method`, `discount_code`, `payment`, `transport_fee`, `receive_timestamp`, `status`, `info`, `is_disable`) VALUES ('3', 'Vinh', '0987654321', '1', 'a', '1', '23245', '200000.000', '20000.000', '2021-10-13 15:46:33', '0', 'note', '0'),('3', 'Hoang', '0987654321', '1', 'a', '1', '23245', '230000.000', '10000.000', '2021-10-13 15:46:33', '0', 'note', '0');
INSERT INTO `BILL_MODEL` VALUES (1,1,1,12,100000.000),(2,2,1,13,1400000.000),(3,1,2,12,100000.000);
SET foreign_key_checks = 1;