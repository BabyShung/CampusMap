<?php

	function initRoutes($inputArr,$type,$howmany,$startfrom,$status)
    {

        
        $result_json = array();
        
        if($status==1){
        		$result_json['status'] = $status; 
        }
           
       	    $temp=new SaeStorage();
        
        
            usort($inputArr,"my_sort");

            //for the first 3 rows in result array , it's sorted by Distance
            $k = $startfrom;
            foreach($inputArr as $rr){
                
                if($k >=$howmany){
                    break;
                }
				
				$result_json['r'.($k+1).'_distance'] = $rr['Distance'];
                $result_json['r'.($k+1).'_time'] = $rr['TakeTime'];
                $result_json['r'.($k+1).'_points'] = $temp->read("routeupload", "routes/".$rr['RouteFileName']);
				$result_json['r'.($k+1).'_type'] = $type;
                $k++;
			}
        
        
        	return $result_json;

  	}


    function appendRoutes($originArr,$appendArr,$type,$howmany){
        
        $temp=new SaeStorage();
        
        if(sizeof($appendArr)>1){
        	usort($appendArr,"my_sort");
        }
        
        $k = 0;
        
        $r_no= (sizeof($originArr)-1)/4+1;
        
            foreach($appendArr as $rr){
                
                if($k >=$howmany){
                    break;
                }
				
				$originArr['r'.($r_no).'_distance'] = $rr['Distance'];
                $originArr['r'.($r_no).'_time'] = $rr['TakeTime'];
                $originArr['r'.($r_no).'_points'] = $temp->read("routeupload", "routes/".$rr['RouteFileName']);
				$originArr['r'.($r_no).'_type'] = $type;
                $k++;
                $r_no++;
			}
        
        
        return $originArr;
    }




    function my_sort($a,$b)
    {
        if ($a['Distance']==$b['Distance']) return 0;
        return ($a['Distance']<$b['Distance'])?-1:1;
    }
 


?>