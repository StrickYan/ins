<?php
/***************************************************************************
 *
 * Copyright (c) 2017 Baidu.com, Inc. All Rights Reserved
 *
 **************************************************************************/

/**
 * @file Application/Home/Controller/IndexController.class.php
 * @author yanjing05(com@baidu.com)
 * @date 2017/04/15 14:22:39
 * @brief
 *
 **/

namespace Home\Controller;

use Home\Common\Client;
use Think\Controller;

class IndexController extends Controller
{
    public function index()
    {
        $this->display();
    }

    public function addStatisticalData()
    {
        $client = new Client();
        $data   = $client->test();
        //$data = $client->testClient($server);

        M('StatisticalData')->add($data);
    }
}
