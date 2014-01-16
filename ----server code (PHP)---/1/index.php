<?php

	require 'login_process.php';

//session_start();
    if(!$_SESSION['id'])//check login status
    {
       header("Location: login.php");
       exit;   
    }
    else{
        $mysql=new SaeMysql();
        $sql="SELECT * FROM Route order by UpdateTime DESC";
        $data=$mysql->getData($sql);
        $mysql->closeDb();
        
    }

    function returnUrl($filename){
        
        $domain = "routeupload";
        $directory = "routes";
        $s=new SaeStorage();
        $fn = $directory.'/'.$filename;
        if($s->fileExists($domain, $fn))
        {
            $url = $s->getUrl($domain, $fn);
            return $url;
        }
        else
            return "#";
    }


?>


<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8"/>
		<title>CampusMap Server</title>
		<!-- Google web fonts -->
		<link href="http://fonts.googleapis.com/css?family=PT+Sans+Narrow:400,700" rel='stylesheet' />
		<!-- The main CSS file -->
		<link href="assets/css/style.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="assets/css/styleList.css" />


	</head>

	<body>

        <div id = "myheader"><h2>Campus Map Server</h2>
         <span class="logout"><a  href="<?php echo $_SESSION['usr'] ? 'login_process.php?logoff=1' : '#';?>" >
             <h3><?php echo $_SESSION['usr'] ? 'Logout' : '';?></h3></a>
            </span>
        </div>
         <div class = "clear"></div>
        

         <?php 
                if($data)
				{
                     echo '<div id="file-manager"><ul class="manager">';
                    echo '<li><div><a>Number of routes: '.count($data).'</a></div>
                            </li>';
                    echo '</ul></div>';
                }
            ?>
            <?php 
        
                if($data)
				{
                    echo '<div id="file-manager"><ul class="manager">';
        			foreach($data as $row){

					    echo '<li><a href='.returnUrl($row['RouteFileName']).' target="_blank" >'.$row['RouteFileName'].'
                        <span class="download-count"  >'.$row['UpdateTime'].'</span> 
                            <span class="download-label">Download</span></a>
                            </li>';
                	}
                    echo '</ul></div>';
                }
            ?>

        
        <form id="upload" method="post" action="post.php" enctype="multipart/form-data">
			<div id="drop">
				Drop Here

				<a>Browse</a>
				<input type="file" name="upl" multiple />
			</div>

			<ul>
				<!-- The file uploads will be shown here -->
			</ul>

		</form>
        
        
        
		<!-- JavaScript Includes -->
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script src="assets/js/jquery.knob.js"></script>

		<!-- jQuery File Upload Dependencies -->
		<script src="assets/js/jquery.ui.widget.js"></script>
		<script src="assets/js/jquery.iframe-transport.js"></script>
		<script src="assets/js/jquery.fileupload.js"></script>
		
		<!-- Our main JS file -->
		<script src="assets/js/script.js"></script>

 

	</body>
</html>