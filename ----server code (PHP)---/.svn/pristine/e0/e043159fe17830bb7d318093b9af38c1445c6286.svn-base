<?php

require_once("route.php");
$type="png_jpg_gif_txt.mama";

$routeInfo = explode("_",trim($type));


function hao(){
$tmp = new Route(1,2,3,4,5,6,7);
return $tmp;
}




   $result=array();
        $i = 0;
        
        //read slat,slng,elat,elng of each row from the db
        $mysql=new SaeMysql();
        $sql="SELECT * FROM Route order by UpdateTime DESC";
        $data=$mysql->getData($sql);
        $mysql->closeDb();
        
        
        
        //for every row, if its starting is 100m within my origin AND its ending is 100m within my des (building center), return this row
        if($data){
        	  foreach($data as $row){
  
                      $result[$i] = $row;
                      $i++;  
                  
              }
                 
        }

		
foreach($result as $r){
 
    echo $r['Distance']."-----";
}

echo "check!!!!!";



foreach ($result as $k => $v) {
  $tArray[$k] = $v['Distance'];
}
$min_value = min($tArray);
echo $min_value;


function my_sort($a,$b)
{
	if ($a['Distance']==$b['Distance']) return 0;
	return ($a['Distance']<$b['Distance'])?-1:1;
}
 

for($i = 0; $i<5;$i++){
    
 	echo "haha";   
}


$json=array();
$json['status'] = array('good'=>10,'bad'=>20);
$json['url'] = "dame";


echo "******".$json['status']['bad'];

//foreach ($json as $k => $v) {
//  echo $k."+".$v;
//}

//usort($result,"my_sort");


//foreach($result as $r){
 
//    echo $r['Distance']."-----";
//}



?>