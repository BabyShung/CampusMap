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
        $result_forth=array();
        $result_fifth=array();
        $i = 0;
        $s_index = 0;
        $t_index = 0;
        $forth_index = 0;
        $fifth_index = 0;
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
                  
       
                  if($start_distance<=10 && $end_distance <= 20){	//type 1
                      
                      
                      $result[$i] = $row;
                      $i++;
                  }
                  else if($start_distance<=30 && $end_distance <= 40){	//type 2
                      
                      
                      $result_second[$s_index] = $row;
                      $s_index++;
                  }
                  else if($start_distance<=50 && $end_distance <= 60){  //type 3  ,68 m
                      $result_third[$t_index] = $row;
                      $t_index++;
                      
                  }
                  /*
                  else if($start_distance<=20 && $end_distance <= 60){	//type 4
                      $result_forth[$forth_index] = $row;
                      $forth_index++;
                      
                  }
                  else if($start_distance<=100 && $end_distance <= 60){	//type 5
                      $result_fifth[$fifth_index] = $row;
                      $fifth_index++;
                      
                  }
                  */
               
                  
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
 
            
            
            if(sizeof($result_second)>0){
                
                $result_json = appendRoutes($result_json,$result_second,2,1);
                
            }else if(sizeof($result_third)>0){
                
                $result_json = appendRoutes($result_json,$result_third,3,1);
                
            }
            else if(sizeof($result_forth)>0){
                
                $result_json = appendRoutes($result_json,$result_forth,4,1);
                
            }
            else if(sizeof($result_fifth)>0){
                
                $result_json = appendRoutes($result_json,$result_fifth,5,1);
                
            }
            
            
            
        }
        else if(sizeof($result)==1){	//need to add two more route from modifying routes
            
            

            
            $result_json = initRoutes($result,1,1,0,1);
            

            
            if(sizeof($result_second)>0){
                
                if(sizeof($result_second)==1){								//append one, need to check result_third
                    
                    $result_json = appendRoutes($result_json,$result_second,2,1);
                    
                    if(sizeof($result_third)>0){
                    
                                $result_json = appendRoutes($result_json,$result_third,3,1);
                        
                    }else if(sizeof($result_forth)>0){
                    
                                $result_json = appendRoutes($result_json,$result_forth,4,1);
                        
                    }else if(sizeof($result_fifth)>0){
                    
                                $result_json = appendRoutes($result_json,$result_fifth,5,1);
                    }
                    
                    
                    
                }else{																	//size>=2, so just append smallest 2
                    $result_json = appendRoutes($result_json,$result_second,2,2);
                    
                    
                }
                
            }else if(sizeof($result_third)>0){		//result_two empty, need two more
                
                
             	   if(sizeof($result_third)==1){								//append one, nothing to check anymore
                       
                       
                    	$result_json = appendRoutes($result_json,$result_third,3,1);
                       
                        if(sizeof($result_forth)>0){
                    
                                $result_json = appendRoutes($result_json,$result_forth,4,1);
                            
                        }else if(sizeof($result_fifth)>0){
                        
                                    $result_json = appendRoutes($result_json,$result_fifth,5,1);
                        }
                       
                   }
                   else{
                           $result_json = appendRoutes($result_json,$result_third,3,2);
                   }
                
            }else if(sizeof($result_forth)>0){		//result_two and three empty, need two more
                
                
                   if(sizeof($result_forth)==1){								//append one, nothing to check anymore
                    
                    	$result_json = appendRoutes($result_json,$result_forth,4,1);
                       
                        if(sizeof($result_fifth)>0){
                    
                                $result_json = appendRoutes($result_json,$result_fifth,5,1);
                        }
                       
                   }
                   else{
                           $result_json = appendRoutes($result_json,$result_forth,4,2);
                   }
            }
            else if(sizeof($result_fifth)>0){		//result_two and three and four empty, need two more
                
                
                   if(sizeof($result_fifth)==1){								//append one, nothing to check anymore
                    
                    	$result_json = appendRoutes($result_json,$result_fifth,5,1);
    
                       
                   }
                   else{
                           $result_json = appendRoutes($result_json,$result_fifth,5,2);
                   }
            }
            

            

        }
        else if(sizeof($result)==0){	//untest

              $result_json = array();


            
             if(sizeof($result_second)>0){
                 
                 $result_json['status'] = 1; 
                 
                 
                if(sizeof($result_second)==1){								//append one, need to check result_third
                    
                    
                            $result_json = appendRoutes($result_json,$result_second,2,1);
                    
                    
                    //----------
                          if(sizeof($result_third)>0){		//result_two empty, need two more
                        
                        
                               if(sizeof($result_third)==1){								//append one, nothing to check anymore
                                   
                                    $result_json = appendRoutes($result_json,$result_third,3,1);
                                   
                                    if(sizeof($result_forth)>0){
                                
                                            $result_json = appendRoutes($result_json,$result_forth,4,1);
                                        
                                    }else if(sizeof($result_fifth)>0){
                                    
                                                $result_json = appendRoutes($result_json,$result_fifth,5,1);
                                    }
                                   
                               }
                               else{
                                       $result_json = appendRoutes($result_json,$result_third,3,2);
                               }
                              
                              
                         }else if(sizeof($result_forth)>0){		//result_two and three empty, need two more
                
                
                                   if(sizeof($result_forth)==1){								//append one, nothing to check anymore
                                    
                                        $result_json = appendRoutes($result_json,$result_forth,4,1);
                                       
                                        if(sizeof($result_fifth)>0){
                                    
                                                $result_json = appendRoutes($result_json,$result_fifth,5,1);
                                        }
                                       
                                   }
                                   else{
                                           $result_json = appendRoutes($result_json,$result_forth,4,2);
                                   }
                              
                        }
                        else if(sizeof($result_fifth)>0){		//result_two and three and four empty, need two more
                            
                            
                               if(sizeof($result_fifth)==1){								//append one, nothing to check anymore
                                
                                    $result_json = appendRoutes($result_json,$result_fifth,5,1);
                                   //only two routes..
                                   
                               }
                               else{
                                       $result_json = appendRoutes($result_json,$result_fifth,5,2);
                               }
                        }
                     //----------
                    
                }else if(sizeof($result_second)==2){
                    
                    //size>=2, so just append smallest 2
                    $result_json = appendRoutes($result_json,$result_second,2,2);
                    
                    
                    if(sizeof($result_third)>0){
                    
                                $result_json = appendRoutes($result_json,$result_third,3,1);
                        
                     }else if(sizeof($result_forth)>0){
                    
                                $result_json = appendRoutes($result_json,$result_forth,4,1);
                        
                     }else if(sizeof($result_fifth)>0){
                    
                                $result_json = appendRoutes($result_json,$result_fifth,5,1);
                     }
                    //done
                }else{
                    $result_json = appendRoutes($result_json,$result_second,2,3);
                }
                
            }else if(sizeof($result_third)>0){		//need three more
                 
                 
                  $result_json['status'] = 1; 
                 
                 
             				  if(sizeof($result_third)==1){								//append one, nothing to check anymore
                      
                      
                                      $result_json = appendRoutes($result_json,$result_third,3,1);
                                   
                                     if(sizeof($result_forth)>0){		//result_two and three empty, need two more
                            
                            
                                               if(sizeof($result_forth)==1){								//append one, nothing to check anymore
                                                
                                                    $result_json = appendRoutes($result_json,$result_forth,4,1);
                                                   
                                                    if(sizeof($result_fifth)>0){
                                                
                                                            $result_json = appendRoutes($result_json,$result_fifth,5,1);
                                                    }
                                                   
                                               }
                                               else{
                                                       $result_json = appendRoutes($result_json,$result_forth,4,2);
                                               }
                                    }
                                    else if(sizeof($result_fifth)>0){		//result_two and three and four empty, need two more
                                        
                                        
                                           if(sizeof($result_fifth)==1){								//append one, nothing to check anymore
                                            
                                                $result_json = appendRoutes($result_json,$result_fifth,5,1);
                                               //only two routes..
                                               
                                           }
                                           else{
                                                   $result_json = appendRoutes($result_json,$result_fifth,5,2);
                                           }
                                    }
                                  
                                  
                                  
                                  
                                  //======
                                   
                               }
                 
                 			   else if(sizeof($result_third)==2){
                                   $result_json = appendRoutes($result_json,$result_third,3,2);
                                   
                                     if(sizeof($result_forth)>0){
                    
                                          $result_json = appendRoutes($result_json,$result_forth,4,1);
                                     }else if(sizeof($result_fifth)>0){
                                    
                                                $result_json = appendRoutes($result_json,$result_fifth,5,1);
                                     }
                                   
                               }
                               else{
                                       $result_json = appendRoutes($result_json,$result_third,3,3);
                               }
                 
                 
                 
                 
            }else if(sizeof($result_forth)>0){		//need three more
                 
                 
                 $result_json['status'] = 1; 
                 
                 
                 
                 							   if(sizeof($result_forth)==1){								//append one, nothing to check anymore
                                                
                                                    $result_json = appendRoutes($result_json,$result_forth,4,1);
                                                   
                                                    if(sizeof($result_fifth)>0){
                                                
                                                             if(sizeof($result_fifth)==1){								//append one, nothing to check anymore
                                            
                                                            	$result_json = appendRoutes($result_json,$result_fifth,5,1);
                                                           		//only two routes..
                                                           
                                                               }
                                                               else{
                                                                       $result_json = appendRoutes($result_json,$result_fifth,5,2);
                                                               }
                                                    }
                                                   
                                               }
                                               else if(sizeof($result_forth)==2){
                                                   
                                                       $result_json = appendRoutes($result_json,$result_forth,4,2);
                                                      
                                                     if(sizeof($result_fifth)>0){
                                                         $result_json = appendRoutes($result_json,$result_fifth,5,1);
                                                     }
                                               }
                                             else{
                                                 
                                                  $result_json = appendRoutes($result_json,$result_forth,4,3);
                                             }
                 
                 //=====
                 
                 
            }
            else if(sizeof($result_fifth)>0){		//need three more
                
                
                 $result_json['status'] = 1; 
                
                
                
                if(sizeof($result_fifth)==1){
                                    
                    $result_json = appendRoutes($result_json,$result_fifth,5,1);
                 }
                else if(sizeof($result_fifth)==2){
                                    
                    $result_json = appendRoutes($result_json,$result_fifth,5,2);
                 }
                else{
                    
                      $result_json = appendRoutes($result_json,$result_fifth,5,3);
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