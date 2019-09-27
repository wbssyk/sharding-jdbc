/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : db1

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 27/09/2019 17:27:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for test_db0
-- ----------------------------
DROP TABLE IF EXISTS `test_db0`;
CREATE TABLE `test_db0`  (
  `id` bigint(100) NOT NULL COMMENT 'id',
  `year` int(4) NULL DEFAULT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `createtime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_db0
-- ----------------------------
INSERT INTO `test_db0` VALUES (382907303847788545, 2000, '10', NULL);
INSERT INTO `test_db0` VALUES (382907304405630977, 2002, '12', NULL);
INSERT INTO `test_db0` VALUES (382907304455962625, 2004, '14', NULL);
INSERT INTO `test_db0` VALUES (382907304497905665, 2006, '16', NULL);
INSERT INTO `test_db0` VALUES (382907304539848705, 2008, '18', NULL);
INSERT INTO `test_db0` VALUES (382907304606957569, 2010, '110', NULL);
INSERT INTO `test_db0` VALUES (382907304644706305, 2012, '112', NULL);
INSERT INTO `test_db0` VALUES (382907304686649345, 2014, '114', NULL);
INSERT INTO `test_db0` VALUES (382907304732786689, 2016, '116', NULL);
INSERT INTO `test_db0` VALUES (382907304770535425, 2018, '118', NULL);

-- ----------------------------
-- Table structure for test_db1
-- ----------------------------
DROP TABLE IF EXISTS `test_db1`;
CREATE TABLE `test_db1`  (
  `id` bigint(100) NOT NULL COMMENT 'id',
  `year` int(4) NULL DEFAULT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `createtime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
