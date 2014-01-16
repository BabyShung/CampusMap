<?php

    require_once("GoogleDistance.php");
	require_once("route_process.php");


    $olat = $_GET['olat'];
	$olng = $_GET['olng'];
	$dlat = $_GET['dlat'];
	$dlng = $_GET['dlng'];	

 


	if(!$olat||!$olng||!$dlat||!$dlng) 
        die('Missing parameter!');
	
 

	if(is_numeric($olat)&&is_numeric($olng)&&is_numeric($dlat)&&is_numeric($dlng))
    {
 
        
        
        //algorithm goes here
        
        //CASE1: routes exist near my location
        
        //make sure origin is my last location, and destination is the building center point
        //check both in the client first, done
        
        
        $result=array();
        $result_second=array();
        $result_third=array();
        $i = 0;
        $s_index = 0;
        $t_index = 0;
        //read slat,slng,elat,elng of each row from the db
        $mysql=new SaeMysql();
        $sql="SELECT * FROM Route order by UpdateTime DESC";
        $data=$mysql->getData($sql);
        $mysql->closeDb();
        
  
        
        //for every row, if its starting is 100m within my origin AND its ending is 100m within my des (building center), return this row
        if($data){
        	  foreach($data as $row){
                  $start_distance = GetDistance($olat,$olng, $row['StartingLat'],$row['StartingLng']);
                  $end_distance = GetDistance($dlat,$dlng, $row['EndingLat'],$row['EndingLng']);
                  
       
                  
                  if($start_distance<=20 && $end_distance <= 50){
                      
                      
                      $result[$i] = $row;
                      $i++;
                  }
                  else if($start_distance<=80 && $end_distance <= 50){
                      
                      
                      $result_second[$s_index] = $row;
                      $s_index++;
                  }
                  else{
                      $result_third[$t_index] = $row;
                      $t_index++;
                      
                  }
               
                  
              }
                 
        }
        
        
        
        
        //if the return list is more than 5, sort them by distance, the time can be estimated by distance/average speed.
        //return the shortest 5 routes.
        //read the corresponding txt file in Storage and return
        
        
        $result_json = array();
        $temp=new SaeStorage();
        
        
        

        
        if(sizeof($result)>=3){	//untest
            
            
            
            $result_json = initRoutes($result,1,3,0,1);
            
            
            




        }
        else if(sizeof($result)==2){	//untest
            
            
            $result_json = initRoutes($result,1,2,0,1);
 
            
            /*
            if(sizeof($result_second)>0){
                
                
                $append = initRoutes($result_second,2,1,9,-1);
                array_push($result_json,$append);
                
            }else if(sizeof($result_third)>0){
                
                $append = initRoutes($result_third,3,1,9,-1);
                array_push($result_json,$append);
                
            }
            */
            
            
        }
        else if(sizeof($result)==1){	//need to add two more route from modifying routes
            
            

            
            $result_json = initRoutes($result,1,1,0,1);
            

            
            if(sizeof($result_second)>0){
                
  
                
            }else if(sizeof($result_third)>0){
                
    
            }
            

        }
        else if(sizeof($result)==0){	//untest

            //.....
            
            
            
            
            $result_json['status'] = 0; 
        }
   
        
        
        //finally output
        echo json_encode($result_json);
        
        //CASE2: routes not near my location

        //echo json_encode(array('status'=>1,'r1_d'=>9,'r1_t'=>3,'r1_p'=>3,'r2_d'=>77,'r2_t'=>3,'r2_p'=>3,'r3_d'=>66,'r3_t'=>3,'r3_p'=>3,'r4_d'=>55,'r4_t'=>3,'r4_p'=>3,'r5_d'=>2,'r5_t'=>3,'r5_p'=>3));
        
        
    }


?>