<?php

    if(isset($_GET['logoff'])) //logoff in main/index.php
    {
        session_start();
        session_destroy();
        
        header("Location: login.php");
        exit;
    }


    if(isset($_POST['email']))
    {

        if(!checkEmail($_POST['email']))
        {
            $err='Invalid Email';
            
        }
        else
        {
            $mysql = new SaeMysql();  
            $_POST['email'] = $mysql->escape($_POST['email']);
            $_POST['password'] = $mysql->escape($_POST['password']);
        
            // Escaping all input data
            $sql = "SELECT * FROM User WHERE Email='".$_POST['email']."' AND UserPWD='".$_POST['password']."'";
            $row = $mysql->getLine( $sql );
            if($row) //如果查询出结果
            {
                
                    // If everything is OK login
                    $usr=$row['UserName'];
                    $id = $row['UserID'];
    
                    $sql2 = "UPDATE User SET lasttime='".date('Y-m-d H:i:s')."' where UserID='".$id."'";
                    $mysql->runSql($sql2);
    
            }
            else 
            {
                $err='Login failed';
            }
            $mysql->closeDb();
        

            if(empty($err)){
                session_name('HaoLogin');
				session_set_cookie_params(60*60);
                session_start();
                $_SESSION['id']=$id;
                $_SESSION['usr']=$usr;
         
            }
    
        }	
    }




	

	//检查email合法
    function checkEmail($str)
    {
        return preg_match("/^[\.A-z0-9_\-\+]+[@][A-z0-9_\-]+([.][A-z0-9_\-]+)+[A-z]{1,4}$/", $str);
    }
?>
