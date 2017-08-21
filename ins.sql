-- --------------------------------------------------------
-- 主机:                           119.29.24.253
-- 服务器版本:                        5.5.48-log - Source distribution
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 search_engine 的数据库结构
CREATE DATABASE IF NOT EXISTS `search_engine` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `search_engine`;


-- 导出  表 search_engine.statistical_data 结构
CREATE TABLE IF NOT EXISTS `statistical_data` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `ip` varchar(50) NOT NULL DEFAULT '' COMMENT '访客ip',
  `location` varchar(50) NOT NULL DEFAULT '' COMMENT '访客所在地区',
  `brower` varchar(50) NOT NULL DEFAULT '' COMMENT '访客所用浏览器',
  `lang` varchar(50) NOT NULL DEFAULT '' COMMENT '访客浏览器语言',
  `os` varchar(50) NOT NULL DEFAULT '' COMMENT '访客使用访问设备的操作系统',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户登录统计数据';

-- 数据导出被取消选择。


-- 导出  表 search_engine.szu_news 结构
CREATE TABLE IF NOT EXISTS `szu_news` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `view_id` int(11) NOT NULL DEFAULT '0' COMMENT '公文通id',
  `title` varchar(200) NOT NULL DEFAULT '' COMMENT '公文通信息的标题',
  `content` text NOT NULL COMMENT '公文通详细内容',
  `from` varchar(100) NOT NULL DEFAULT '' COMMENT '公文通发文单位',
  `date` varchar(50) NOT NULL DEFAULT '' COMMENT '公文通发布时间',
  `click_in_content` int(11) NOT NULL DEFAULT '0' COMMENT '公文通内统计的点击次数',
  `click` int(11) NOT NULL DEFAULT '0' COMMENT '点击次数',
  `impression` int(11) NOT NULL DEFAULT '0' COMMENT '曝光次数',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `view_id` (`view_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公文通信息';

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
