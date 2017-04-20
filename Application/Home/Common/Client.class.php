<?php
/***************************************************************************
 *
 * Copyright (c) 2017 Baidu.com, Inc. All Rights Reserved
 *
 **************************************************************************/

/**
 * @file Application/Home/Common/Client.class.php
 * @author yanjing05(com@baidu.com)
 * @date 2017/04/17 13:22:39
 * @brief 解析$_SERVER获取客户端访问的信息
 *
 **/

namespace Home\Common;

class Client
{
    protected $server;

    public function __construct()
    {
        $this->server   = $_SERVER;
        $this->ip       = "";
        $this->location = "";
        $this->brower   = "";
        $this->lang     = "";
        $this->os       = "";
    }

    // 获得访客浏览器类型
    public function GetBrowser()
    {
        if (!empty($this->server['HTTP_USER_AGENT'])) {
            $br = $this->server['HTTP_USER_AGENT'];
            if (preg_match('/MSIE/i', $br)) {
                $br = 'MSIE';
            } elseif (preg_match('/Firefox/i', $br)) {
                $br = 'Firefox';
            } elseif (preg_match('/Chrome/i', $br)) {
                $br = 'Chrome';
            } elseif (preg_match('/Safari/i', $br)) {
                $br = 'Safari';
            } elseif (preg_match('/Opera/i', $br)) {
                $br = 'Opera';
            } else {
                $br = 'Other';
            }
            return $br;
        } else {return false;}
    }

    // 获得访客浏览器语言
    public function GetLang()
    {
        if (!empty($this->server['HTTP_ACCEPT_LANGUAGE'])) {
            $lang = $this->server['HTTP_ACCEPT_LANGUAGE'];
            $lang = substr($lang, 0, 5);
            if (preg_match("/zh-cn/i", $lang)) {
                $lang = "简体中文";
            } elseif (preg_match("/zh/i", $lang)) {
                $lang = "繁体中文";
            } else {
                $lang = "English";
            }
            return $lang;

        } else {return false;}
    }

    // 获取访客操作系统
    public function GetOs()
    {
        if (!empty($this->server['HTTP_USER_AGENT'])) {
            $OS = $this->server['HTTP_USER_AGENT'];
            if (preg_match('/win/i', $OS)) {
                $OS = 'Windows';
            } elseif (preg_match('/mac/i', $OS)) {
                $OS = 'MAC';
            } elseif (preg_match('/linux/i', $OS)) {
                $OS = 'Linux';
            } elseif (preg_match('/unix/i', $OS)) {
                $OS = 'Unix';
            } elseif (preg_match('/bsd/i', $OS)) {
                $OS = 'BSD';
            } else {
                $OS = 'Other';
            }
            return $OS;
        } else {return false;}
    }

    // 获得访客真实ip
    public function Getip()
    {
        if (!empty($this->server["HTTP_CLIENT_IP"])) {
            $ip = $this->server["HTTP_CLIENT_IP"];
        }
        if (!empty($this->server['HTTP_X_FORWARDED_FOR'])) {
            //获取代理ip
            $ips = explode(',', $this->server['HTTP_X_FORWARDED_FOR']);
        }
        if ($ip) {
            $ips = array_unshift($ips, $ip);
        }

        $count = count($ips);
        for ($i = 0; $i < $count; $i++) {
            if (!preg_match("/^(10|172\.16|192\.168)\./i", $ips[$i])) {
                //排除局域网ip
                $ip = $ips[$i];
                break;
            }
        }
        $tip = empty($this->server['REMOTE_ADDR']) ? $ip : $this->server['REMOTE_ADDR'];
        if ($tip == "127.0.0.1" || $tip == "::1") {
            //获得本地真实IP
            return $this->get_onlineip();
        } else {
            return $tip;
        }
    }

    // 获得本地真实IP
    public function get_onlineip()
    {
        $html = file_get_contents("http://1212.ip138.com/ic.asp");
        if ($html) {
            $html   = iconv('gb2312', 'utf-8', $html);
            $result = array();
            preg_match_all("/(?:\[)(.*)(?:\])/i", $html, $result);
            return $result[1][0];
        } else {return false;}
    }

    // 根据ip获得访客所在地地名
    public function getIpAddress($ip = '')
    {
        $ipContent   = file_get_contents("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=" . $ip);
        $jsonData    = explode("=", $ipContent);
        $jsonAddress = substr($jsonData[1], 0, -1);
        $ip_info     = json_decode($jsonAddress, true);
        $location    = $ip_info['country'] . $ip_info['province'] . $ip_info['city'];
        return $location;
    }

    // 解析$_SERVER, 得到相关结果
    public function test()
    {
        $this->ip       = $this->Getip();
        $this->location = $this->getIpAddress($this->ip);
        $this->brower   = $this->GetBrowser();
        $this->lang     = $this->GetLang();
        $this->os       = $this->GetOs();

        $data = array(
            'ip'       => $this->ip, //ip
            'location' => $this->location, //ip所在地
            'brower'   => $this->brower, //浏览器类型
            'lang'     => $this->lang, //浏览器语言
            'os'       => $this->os, //操作系统
        );
        return $data;
    }

    // 解析自定义的$server, 得到相关结果
    public function testClient($server = array())
    {
        $this->server   = $server;
        $this->ip       = $this->Getip();
        $this->location = $this->getIpAddress($this->ip);
        $this->brower   = $this->GetBrowser();
        $this->lang     = $this->GetLang();
        $this->os       = $this->GetOs();

        $data = array(
            'ip'       => $this->ip, //ip
            'location' => $this->location, //ip所在地
            'brower'   => $this->brower, //浏览器类型
            'lang'     => $this->lang, //浏览器语言
            'os'       => $this->os, //操作系统
        );
        return $data;
    }

}
