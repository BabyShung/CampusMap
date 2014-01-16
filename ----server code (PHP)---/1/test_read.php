<?php
function sae_write($file,$content){
file_put_contents(SAE_TMP_PATH."/".$file,$content);
}
function sae_read($file){
return file_get_contents(SAE_TMP_PATH."/".$file);
}
?>


<?php
sae_write("1.txt","周春");
$rs=sae_read("1.txt");
print_r($rs);


$temp=new SaeStorage();
echo $temp->getUrl("routeupload", "uploaded_20131210123646_2cedcded8e9b2b98a2fb91.txt");


echo $temp->read("routeupload", "routes/uploaded_20131210123646_2cedcded8e9b2b98a2fb91.txt");

echo $temp->getAttr("routeupload", "routes/uploaded_20131210123646_2cedcded8e9b2b98a2fb91.txt");


?>