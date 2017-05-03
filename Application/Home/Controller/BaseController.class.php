<?php
/***************************************************************************
 *
 * Copyright (c) 2017 Baidu.com, Inc. All Rights Reserved
 *
 **************************************************************************/

/**
 * @file Application/Home/Controller/BaseController.class.php
 * @author yanjing05(com@baidu.com)
 * @date 2017/04/15 14:22:39
 * @brief
 *
 **/

namespace Home\Controller;

use Home\Common\Log;
use Think\Controller;

class BaseController extends Controller
{

    /**
     *  @brief
     *  @params
     *  @return |
     */
    public function returnData($code, $data = array(), $msg = '')
    {
        $data = isset($data) ? $data : array();
        if (empty($msg)) {
            //$msg = ErrorCode::getErrorMsg($code);
            $msg = $code;
        }
        Log::pushNotice('retCode', $code);
        Log::pushNotice('retMsg', $msg);
        echo json_encode(array(
            'retCode' => $code,
            'retMsg'  => $msg,
            'retData' => $data,
        ));
        exit;
    }

}
