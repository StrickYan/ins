<?php
namespace Home\Controller;

use Home\Common\Client;
use Think\Controller;

class IndexController extends Controller
{
    public function index()
    {
        // M('StatisticalData')->where('id=1')->setInc('login_times', 1);
        // $script = "<script>var server = " . json_encode($_SERVER) . ";</script>";
        // $this->assign('server', $script);
        $this->display();
    }

    public function addStatisticalData()
    {
    	// $server = I('server', '');
    	// if (!is_array($server)) {
    	// 	$server = json_decode($server, true);
    	// }
    	
    	$client = new Client();
    	$data = $client->test();
    	//$data = $client->testClient($server);

    	M('StatisticalData')->add($data);

    }
}
