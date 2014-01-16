<?php

    require_once("GoogleDistance.php");
	require_once("route_process.php");

 
    $olat = 41.659234;
	$olng = -91.5379349;
	$dlat = 41.659378;
	$dlng = -91.535447;	

 
 
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
                  else if($start_distance<=80 && $end_distance <= 80){
                      
                      
                      $result_second[$s_index] = $row;
                      $s_index++;
                  }
                  else if($start_distance<=160 && $end_distance <= 50){
                      $result_third[$t_index] = $row;
                      $t_index++;
                      
                  }
               
                  
              }
                 
        }
        
        echo sizeof($result)."</br>";
        echo sizeof($result_second)."</br>";
        echo sizeof($result_third);
        
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
 
            
            
            if(sizeof($result_second)>0){
                
                $result_json = appendRoutes($result_json,$result_second,2,1);
                
            }else if(sizeof($result_third)>0){
                
                $result_json = appendRoutes($result_json,$result_second,3,1);
                
            }
            
            
            
        }
        else if(sizeof($result)==1){	//need to add two more route from modifying routes
            
            

            
            $result_json = initRoutes($result,1,1,0,1);
            

            
            if(sizeof($result_second)>0){
                
                if(sizeof($result_second)==1){								//append one, need to check result_third
                    $result_json = appendRoutes($result_json,$result_second,2,1);
                    
                    if(sizeof($result_third)>0){
                    
                                $result_json = appendRoutes($result_json,$result_third,3,1);
                     }
                    
                    
                }else{																	//size>=2, so just append smallest 2
                    $result_json = appendRoutes($result_json,$result_second,2,2);
                    
                    
                }
                
            }else if(sizeof($result_third)>0){		//need two more
             	   if(sizeof($result_third)==1){								//append one, nothing to check anymore
                    	$result_json = appendRoutes($result_json,$result_third,3,1);
                       //return ..?
                   }
                else{
                        $result_json = appendRoutes($result_json,$result_third,3,2);
                }
            }
            
            

            

        }
        else if(sizeof($result)==0){	//untest

              $result_json = array();


            
             if(sizeof($result_second)>0){
                 
                 $result_json['status'] = 1; 
                 
                 
                if(sizeof($result_second)==1){								//append one, need to check result_third
                    
                    
                    $result_json = appendRoutes($result_json,$result_second,2,1);
                    
                            if(sizeof($result_third)==1){								//append one, nothing to check anymore
                                $result_json = appendRoutes($result_json,$result_third,3,1);
                               //return ..?
                           }
                            else{
                                    $result_json = appendRoutes($result_json,$result_third,3,2);
                            }
                    
                    
                }else if(sizeof($result_second)==2){																	//size>=2, so just append smallest 2
                    $result_json = appendRoutes($result_json,$result_second,2,2);
                    if(sizeof($result_third)>0){
                    
                                $result_json = appendRoutes($result_json,$result_third,3,1);
                     }
                    
                }else{
                    $result_json = appendRoutes($result_json,$result_second,2,3);
                }
                
            }else if(sizeof($result_third)>0){		//need three more
                 
                 
                  $result_json['status'] = 1; 
                 
                 
             	   if(sizeof($result_third)==1){								//append one, nothing to check anymore
                    	$result_json = appendRoutes($result_json,$result_third,3,1);
                       //return ..?
                   }
                   else if(sizeof($result_third)==2){
                       $result_json = appendRoutes($result_json,$result_third,3,2);
                   }
                    else{
                            $result_json = appendRoutes($result_json,$result_third,3,3);
                    }
            }
            else{

	            $result_json['status'] = 0; 
                
            }
            

        }
   
        
        
        //finally output
        echo json_encode($result_json);
        
        //CASE2: routes not near my location
 
        
    }


?>