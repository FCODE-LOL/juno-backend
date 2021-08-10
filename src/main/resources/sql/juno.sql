-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema junodb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema junodb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `junodb` DEFAULT CHARACTER SET utf8mb4 ;
USE `junodb` ;

-- -----------------------------------------------------
-- Table `junodb`.`BILL`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`BILL` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `orderer_name` VARCHAR(45) NULL DEFAULT NULL,
  `phone` VARCHAR(45) NULL DEFAULT NULL,
  `area_id` INT(11) NULL DEFAULT NULL,
  `address` VARCHAR(45) NULL DEFAULT NULL,
  `payment_method_id` INT(11) NULL DEFAULT NULL,
  `discount_code` VARCHAR(10) NULL DEFAULT NULL,
  `transport_fee` DECIMAL(20,3) NULL DEFAULT NULL,
  `is_disable` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `junodb`.`PRODUCT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`PRODUCT` (
  `id` VARCHAR(6) NOT NULL,
  `name` VARCHAR(60) NULL DEFAULT NULL,
  `link_images` TEXT NULL DEFAULT NULL,
  `colors_id` VARCHAR(90) NULL DEFAULT NULL,
  `type_id` TINYINT(4) NULL DEFAULT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `origin` VARCHAR(60) NULL DEFAULT NULL,
  `material` VARCHAR(45) NULL DEFAULT NULL,
  `size` VARCHAR(30) NULL DEFAULT NULL,
  `total_quantity` INT(11) NULL DEFAULT NULL,
  `curremt_quantity` INT(11) NULL DEFAULT NULL,
  `price` DECIMAL(20,3) NULL DEFAULT NULL,
  `created_timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_gettable` TINYINT(1) NULL DEFAULT NULL,
  `is_disable` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `junodb`.`BILL_PRODUCT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`BILL_PRODUCT` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `BILL_id` INT(11) NULL DEFAULT NULL,
  `PRODUCT_id` VARCHAR(6) NULL DEFAULT NULL,
  `quantity` INT(11) NULL DEFAULT NULL,
  `color_id` INT(11) NULL DEFAULT NULL,
  `size_id` INT(11) NULL DEFAULT NULL,
  `is_disable` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `BILL_id` (`BILL_id` ASC),
  INDEX `PRODUCT_id` (`PRODUCT_id` ASC),
  CONSTRAINT `BILL_PRODUCT_ibfk_1`
    FOREIGN KEY (`BILL_id`)
    REFERENCES `junodb`.`BILL` (`id`),
  CONSTRAINT `BILL_PRODUCT_ibfk_2`
    FOREIGN KEY (`PRODUCT_id`)
    REFERENCES `junodb`.`PRODUCT` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `junodb`.`DISCOUNT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`DISCOUNT` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NULL DEFAULT NULL,
  `price` DECIMAL(20,3) NULL DEFAULT NULL,
  `percent` TINYINT(4) NULL DEFAULT NULL,
  `start_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_disable` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `junodb`.`PRODUCT_DISCOUNT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`PRODUCT_DISCOUNT` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `PRODUCT_id` VARCHAR(6) NULL DEFAULT NULL,
  `DISCOUNT_id` INT(11) NULL DEFAULT NULL,
  `start_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_disable` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `PRODUCT_id` (`PRODUCT_id` ASC),
  INDEX `DISCOUNT_id` (`DISCOUNT_id` ASC),
  CONSTRAINT `PRODUCT_DISCOUNT_ibfk_1`
    FOREIGN KEY (`PRODUCT_id`)
    REFERENCES `junodb`.`PRODUCT` (`id`),
  CONSTRAINT `PRODUCT_DISCOUNT_ibfk_2`
    FOREIGN KEY (`DISCOUNT_id`)
    REFERENCES `junodb`.`DISCOUNT` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `junodb`.`USER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`USER` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `password` VARCHAR(70) NULL DEFAULT NULL,
  `phone` CHAR(10) NULL DEFAULT NULL,
  `name` VARCHAR(20) NULL DEFAULT NULL,
  `date_of_birth` CHAR(10) NULL DEFAULT NULL,
  `rank_id` INT(11) NULL DEFAULT NULL,
  `is_admin` TINYINT(1) NULL DEFAULT NULL,
  `area_id` INT(11) NULL DEFAULT NULL,
  `address` VARCHAR(60) NULL DEFAULT NULL,
  `register_timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `token` VARCHAR(45) NULL DEFAULT NULL,
  `token_timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `point` INT(11) NULL DEFAULT NULL,
  `social_media_id` VARCHAR(45) NULL,
  `is_disable` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `junodb`.`USER_BILL`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`USER_BILL` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `USER_id` INT(11) NULL DEFAULT NULL,
  `BILL_id` INT(11) NULL DEFAULT NULL,
  `is_disable` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `USER_id` (`USER_id` ASC),
  INDEX `BILL_id` (`BILL_id` ASC),
  CONSTRAINT `USER_BILL_ibfk_1`
    FOREIGN KEY (`USER_id`)
    REFERENCES `junodb`.`USER` (`id`),
  CONSTRAINT `USER_BILL_ibfk_2`
    FOREIGN KEY (`BILL_id`)
    REFERENCES `junodb`.`BILL` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;