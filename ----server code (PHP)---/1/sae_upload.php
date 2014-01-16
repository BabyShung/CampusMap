<?php

require_once("route.php");

class sae_upload{
    
	public $domain="routeupload";//域
	public $path="routes";//上传目录
	public $type="png|jpg|gif|txt";//文件类型
	public $name="upl";//表单名称
	public $save_name;//保存文件名
    public $save_name_with_extension;
    
	public function __construct($save_name=""){
		$this->save_name=$save_name;
	}
	
	public function upload(){
        

        
		$result=array();//返回的数据
        
		$basename=basename($_FILES[$this->name]["name"]);//原始文件名
        

        
        $routeInfo = explode("@",trim($basename));
        // process all the info
       

        
        
		$extension=pathinfo($basename,PATHINFO_EXTENSION);//拓展名
        
		$data=explode("|",trim(strtolower($this->type)));//允许的上传类型转为数组
        

		if(in_array($extension,$data)){
 
			
            $upload_path=SAE_TMP_PATH.$this->path;
            
			move_uploaded_file($_FILES[$this->name]["tmp_name"],$upload_path);
			
            $content=file_get_contents($upload_path);
			
			$temp=new SaeStorage();
			
			if($this->save_name==""){
				
                $filename=$this->path."/".$basename;
                
                $this->save_name = $basename;
			}//原名称保存
			else{
				$filename=$this->path."/".$this->save_name.".".$extension;
			}//按传入的名称保存
			
            $save_name_with_extension = $this->save_name.".".$extension;
                        
			$temp->write($this->domain,$filename,$content);//写入文件
			
			$url=$temp->getUrl($this->domain,$filename);//获取地址

			$property=$temp->getAttr($this->domain,$filename);//获取文件属性
			
			$result["url"]=$url;
			
			$result["property"]=$property;
			
			$result["success"]="1";
            
            
            $route_obj = new Route($routeInfo[0],$routeInfo[1],$routeInfo[2],$routeInfo[3],$routeInfo[4],$routeInfo[5],$routeInfo[6],$save_name_with_extension);
 
            
            return $route_obj;
            
		}
		else{
			$result["success"]="0";
            return 0;
		}
        
        
	}
}
?>