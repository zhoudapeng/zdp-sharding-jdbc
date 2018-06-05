create database trans_shard_0;
use trans_shard_0;
CREATE TABLE `trans_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trans_id` varchar(30) NOT NULL DEFAULT '' COMMENT '订单号',
  `trans_name` varchar(30) NOT NULL DEFAULT '' COMMENT '订单名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_trans_id` (`trans_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8

CREATE TABLE `trans_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trans_id` varchar(30) NOT NULL DEFAULT '' COMMENT '订单号',
  `trans_name` varchar(30) NOT NULL DEFAULT '' COMMENT '订单名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_trans_id` (`trans_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8

create database trans_shard_1;
use trans_shard_1;
CREATE TABLE `trans_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trans_id` varchar(30) NOT NULL DEFAULT '' COMMENT '订单号',
  `trans_name` varchar(30) NOT NULL DEFAULT '' COMMENT '订单名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_trans_id` (`trans_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8

CREATE TABLE `trans_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trans_id` varchar(30) NOT NULL DEFAULT '' COMMENT '订单号',
  `trans_name` varchar(30) NOT NULL DEFAULT '' COMMENT '订单名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_trans_id` (`trans_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8