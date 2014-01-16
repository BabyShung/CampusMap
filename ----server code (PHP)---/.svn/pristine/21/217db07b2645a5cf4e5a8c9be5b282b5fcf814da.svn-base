<?php


if(!$_GET['file']) die('Missing parameter!');
if($_GET['file']{0}=='.') die('Wrong file!');


$domain = "routeupload";
$directory = "routes";
$fn = $directory."/".$_GET['file'];

echo $fn;

$s=new SaeStorage();

if($s->fileExists($domain, $fn))
{
    $url = $s->getUrl($domain, $fn);
    header("Location: ".$url);
    exit;
}
else error("This file does not exist!");


/* Helper functions: */

function error($str)
{
	die($str);
}


function is_bot()
{
	/* This function will check whether the visitor is a search engine robot */
	
	$botlist = array("Teoma", "alexa", "froogle", "Gigabot", "inktomi",
	"looksmart", "URL_Spider_SQL", "Firefly", "NationalDirectory",
	"Ask Jeeves", "TECNOSEEK", "InfoSeek", "WebFindBot", "girafabot",
	"crawler", "www.galaxy.com", "Googlebot", "Scooter", "Slurp",
	"msnbot", "appie", "FAST", "WebBug", "Spade", "ZyBorg", "rabaz",
	"Baiduspider", "Feedfetcher-Google", "TechnoratiSnoop", "Rankivabot",
	"Mediapartners-Google", "Sogou web spider", "WebAlta Crawler","TweetmemeBot",
	"Butterfly","Twitturls","Me.dium","Twiceler");

	foreach($botlist as $bot)
	{
		if(strpos($_SERVER['HTTP_USER_AGENT'],$bot)!==false)
		return true;	// Is a bot
	}

	return false;	// Not a bot
}
?>