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
-- Table `junodb`.`USER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`USER` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `password` VARCHAR(70) NULL DEFAULT NULL,
  `phone` CHAR(10) NULL DEFAULT NULL,
  `name` VARCHAR(20) NULL DEFAULT NULL,
  `date_of_birth` CHAR(10) NULL DEFAULT NULL,
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
-- Table `junodb`.`BILL`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`BILL` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `USER_id` INT(11) NULL DEFAULT NULL,
  `customer_name` VARCHAR(45) NULL DEFAULT NULL,
  `phone` VARCHAR(45) CHARACTER SET 'cp850' NULL DEFAULT NULL,
  `area_id` INT(11) NULL DEFAULT NULL,
  `address` VARCHAR(45) NULL DEFAULT NULL,
  `payment_method` INT(11) NULL DEFAULT NULL,
  `discount_code` VARCHAR(10) NULL DEFAULT NULL,
  `payment` DECIMAL(20,3) NULL,
  `transport_fee` DECIMAL(20,3) NULL DEFAULT NULL,
  `created_timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `receive_timestamp` TIMESTAMP NULL,
  `status` TINYINT NULL,
  `info` TEXT NULL,
  `is_disable` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_BILL_USER1_idx` (`USER_id` ASC),
  CONSTRAINT `fk_BILL_USER1`
    FOREIGN KEY (`USER_id`)
    REFERENCES `junodb`.`USER` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `junodb`.`TYPE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`TYPE` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `parent_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_TYPE_TYPE1_idx` (`parent_id` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  CONSTRAINT `fk_TYPE_TYPE1`
    FOREIGN KEY (`parent_id`)
    REFERENCES `junodb`.`TYPE` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `junodb`.`PRODUCT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`PRODUCT` (
  `id` VARCHAR(10) NOT NULL,
  `name` VARCHAR(60) NULL DEFAULT NULL,
  `link_images` TEXT NULL DEFAULT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `origin` VARCHAR(60) NULL DEFAULT NULL,
  `material` VARCHAR(45) NULL DEFAULT NULL,
  `TYPE_id` INT NULL,
  `price` DECIMAL(20,3) NULL DEFAULT NULL,
  `discount_price` DECIMAL(20,3) NULL,
  `created_timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_gettable` TINYINT(1) NULL DEFAULT NULL,
  `is_disable` TINYINT(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_PRODUCT_TYPE1_idx` (`TYPE_id` ASC),
  CONSTRAINT `fk_PRODUCT_TYPE1`
    FOREIGN KEY (`TYPE_id`)
    REFERENCES `junodb`.`TYPE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `junodb`.`MODEL`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`MODEL` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `PRODUCT_id` VARCHAR(10) NOT NULL,
  `color_id` VARCHAR(10) NULL DEFAULT NULL,
  `link_images` TEXT NULL DEFAULT NULL,
  `size` INT(11) NULL DEFAULT NULL,
  `quantity` INT(11) NULL DEFAULT NULL,
  `price` DECIMAL(20,3) NULL DEFAULT NULL,
  `discount_price` DECIMAL(20,3) NULL,
  `is_disable` TINYINT(1) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_MODEL_PRODUCT1_idx` (`PRODUCT_id` ASC),
  CONSTRAINT `fk_MODEL_PRODUCT1`
    FOREIGN KEY (`PRODUCT_id`)
    REFERENCES `junodb`.`PRODUCT` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `junodb`.`BILL_PRODUCT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`BILL_PRODUCT` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `BILL_id` INT(11) NOT NULL,
  `MODEL_id` INT NOT NULL,
  `quantity` INT(11) NULL DEFAULT NULL,
  `price` DECIMAL(20,3) NULL,
  PRIMARY KEY (`id`, `BILL_id`, `MODEL_id`),
  INDEX `BILL_id` (`BILL_id` ASC),
  INDEX `fk_BILL_PRODUCT_MODEL1_idx` (`MODEL_id` ASC),
  CONSTRAINT `BILL_PRODUCT_ibfk_1`
    FOREIGN KEY (`BILL_id`)
    REFERENCES `junodb`.`BILL` (`id`),
  CONSTRAINT `fk_BILL_PRODUCT_MODEL1`
    FOREIGN KEY (`MODEL_id`)
    REFERENCES `junodb`.`MODEL` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
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
  `is_disable` TINYINT(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `junodb`.`DISCOUNT_MODEL`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `junodb`.`DISCOUNT_MODEL` (
  `id` INT(11) NOT NULL,
  `DISCOUNT_id` INT(11) NULL DEFAULT NULL,
  `MODEL_id` INT NOT NULL,
  `quantity` INT(11) NULL,
  PRIMARY KEY (`id`),
  INDEX `DISCOUNT_id` (`DISCOUNT_id` ASC),
  INDEX `fk_PRODUCT_DISCOUNT_MODEL1_idx` (`MODEL_id` ASC),
  CONSTRAINT `PRODUCT_DISCOUNT_ibfk_2`
    FOREIGN KEY (`DISCOUNT_id`)
    REFERENCES `junodb`.`DISCOUNT` (`id`),
  CONSTRAINT `fk_PRODUCT_DISCOUNT_MODEL1`
    FOREIGN KEY (`MODEL_id`)
    REFERENCES `junodb`.`MODEL` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
