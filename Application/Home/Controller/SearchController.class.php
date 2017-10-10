<?php
/***************************************************************************
 *
 * Copyright (c) 2017 Baidu.com, Inc. All Rights Reserved
 *
 **************************************************************************/

/**
 * @file Application/Home/Controller/SearchController.class.php
 * @author yanjing05(com@baidu.com)
 * @date 2017/04/15 14:22:39
 * @brief
 *
 **/

namespace Home\Controller;

use Think\Controller;

class SearchController extends BaseController
{

    /**
     * @brief 渲染搜索页面结构
     * @param
     * @return [type] [description]
     */
    public function index()
    {
        $input['q']    = I('q', '');
        $input['p']    = I('p', 1, 'intval');
        $input['sort'] = I('sort', '');

        $script = "<script>var page = " . $input['p'] . ";var sort = '" . $input['sort'] . "';</script>";

        $this->assign('input', $input);
        $this->assign('script_page', $script);
        $this->display();
    }

    /**
     * @brief 获取搜索结果
     * @param
     * @return [type] [description]
     */
    public function getQueryResult()
    {
        $input['q']    = I('q', '');
        $input['p']    = I('p', 1, 'intval');
        $input['sort'] = I('sort', '');

        $url      = "http://localhost:8070/ins/search";
        $response = self::postData($url, $input);
        $response = json_decode($response, true); //json转array

        if ($response['state'] == 'true') {
            $this->returnData($code = 0, $data = $response, $msg = 'Successed.');
        } else {
            $this->returnData($code = -1, $data = array(), $msg = 'Failed.');
        }

    }

    /**
     * @brief 调用 api 接口，获得返回的数据
     * @param $url | api接口的url地址
     * @param $data | url参数，数组形式
     * @return [type] [description]
     */
    public function postData($url, $data = array())
    {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        //curl_setopt($ch, CURLOPT_POST, true); //true为post请求
        curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        $rets = curl_exec($ch);
        curl_close($ch);
        return $rets;
    }

    /**
     * @brief 某条搜索结果详情页
     * @param
     * @return [type] [description]
     */
    public function details()
    {
        $input['view_id'] = I('id', 0, 'intval');

        $script = "<script>var view_id = " . $input['view_id'] . ";</script>";

        $this->assign('input', $input);
        $this->assign('script_page', $script);
        $this->display();
    }

    /**
     * @brief 获取某条搜索结果详情
     * @param
     * @return [type] [description]
     */
    public function getQueryResultDetails()
    {
        $input['view_id'] = I('view_id', 0, 'intval');

        $model = M('SzuNews');
        $model->startTrans();
        $ret   = $model->where($input)->setInc('click', 1); //click+1
        if ($ret === false) {
        	$model->rollback();
        	$this->returnData($code = -1, $data = array(), $msg = 'Update click Failed.');
        }
        $model->commit();

        $start = time();

        $ret = $model->where($input)->find();

        $end = time();

        if ($ret === false) {
            $this->returnData($code = -1, $data = array(), $msg = 'Get details Failed.');
        }

        $data_show = array(
            // 'content' => $ret['content'],
            'content' => preg_replace('/<img[^>]+>/i','', $ret['content']), //过滤img标签
            'time'    => ($end - $start),
        );

        $this->returnData($code = 0, $data = $data_show, $msg = 'Successed.');
    }

}
