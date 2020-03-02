
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table block_cache
# ------------------------------------------------------------

CREATE TABLE `block_cache` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `block_hash` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `height` bigint(20) unsigned NOT NULL,
  `block_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_block_hash` (`block_hash`) USING BTREE,
  KEY `idx_height` (`height`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



# Dump of table block_height
# ------------------------------------------------------------

CREATE TABLE `block_height` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `node_latest_block_height` bigint(20) unsigned DEFAULT '0',
  `latest_block_height` bigint(20) unsigned DEFAULT '0',
  `last_call_back_height` bigint(20) unsigned DEFAULT NULL,
  `last_call_back_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



# Dump of table msg_queue
# ------------------------------------------------------------

CREATE TABLE `msg_queue` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `is_callback` tinyint(1) unsigned DEFAULT '0',
  `msg` mediumtext COLLATE utf8mb4_bin NOT NULL,
  `msg_type` smallint(2) unsigned NOT NULL,
  `last_callback_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `height` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`) USING BTREE COMMENT '先入先出队列',
  KEY `idx_height` (`height`) USING BTREE COMMENT '高度索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



# Dump of table sync_height
# ------------------------------------------------------------

CREATE TABLE `sync_height` (
  `id` bigint(20) unsigned NOT NULL,
  `sync_height` bigint(20) unsigned NOT NULL COMMENT '区块高度',
  `block_hash` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '对应高度的区块hash',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
