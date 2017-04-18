<?php
return array(
	//'配置项'=>'配置值'
	'URL_HTML_SUFFIX' => 'html',  // URL伪静态后缀设置
	//数据库连接
    'DB_TYPE'   => 'mysql', // 数据库类型
    'DB_HOST'   => 'localhost', // 服务器地址
    'DB_NAME'   => 'search_engine', // 数据库名
    'DB_USER'   => 'root', // 用户名
    'DB_PWD'    => '', // 密码
    'DB_PORT'   => 3306, // 端口
    'DB_PREFIX' => '', // 数据库表前缀 
    'DB_CHARSET'=> 'utf8mb4', // 字符集
    'URL_MODEL' => '2',//url省略index.php
    'DEFAULT_FILTER' => 'strip_tags,htmlspecialchars,trim'
);