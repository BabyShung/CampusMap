<?php

   

    function rad($d)
    {
       return $d * pi() / 180.0;
    }

    //unit: M
    function GetDistance($lat1, $lng1, $lat2, $lng2)
    {
        $EARTH_RADIUS = 6378.137; 
        
       $radLat1 = rad($lat1);
       
 
       $radLat2 = rad($lat2);
        
       $a = $radLat1 - $radLat2;
       
        
       $b = rad($lng1) - rad($lng2);
        
       
       $s = 2 * asin(sqrt(pow(sin($a/2),2) + cos($radLat1)*cos($radLat2)*pow(sin($b/2),2)));

       $s = $s * $EARTH_RADIUS;
        
        
       $s =  round($s*1000);

       return $s;
    }


	

?>