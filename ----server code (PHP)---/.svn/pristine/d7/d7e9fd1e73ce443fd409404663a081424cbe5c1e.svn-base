<?php
//load class
require_once("sae_upload.php");


//unique key
$key = md5(microtime().rand());   
$key = substr($key,10);



if(isset($_FILES["upl"])){

    $filename=uploaded."_".date("YmdHis")."_".$key;//文件保存名称[没有拓展名]
    
    $upload=new sae_upload($filename);//参数为空或者不传参数的话将按原始文件名保存
    
    $return_obj = $upload->upload();//不管成功与否都会返回一个数组

    if($return_obj!=0){
    	//also insert into server Database
    	 $mysql = new SaeMysql();
    	 $sql = "	INSERT INTO Route(RouteFileName,StartingLat,StartingLng,EndingLat,EndingLng,Distance,TakeTime,Destination,UpdateTime)
						VALUES(
							'".$mysql->escape($return_obj->filename)."',
							".$mysql->escape($return_obj->start_lat).",
							".$mysql->escape($return_obj->start_lng).",
							".$mysql->escape($return_obj->end_lat).",
                            ".$mysql->escape($return_obj->end_lng).",
							".$mysql->escape($return_obj->distance).",
							".$mysql->escape($return_obj->taketime).",
                            '".$mysql->escape($return_obj->destination)."',
							'".date('Y-m-d H:i:s')."'
						)";
    	 $mysql->runSql($sql);	
    	 $mysql->closeDb();
    }
    
}
else 
    die("Sorry, can't visit");
?>