$(function(){
	
	// container is the DOM element;
	// userText is the textbox
	var d=new Date();
var weekday=new Array(7);
weekday[0]="Sunday";
weekday[1]="Monday";
weekday[2]="Tuesday";
weekday[3]="Wednesday";
weekday[4]="Thursday";
weekday[5]="Friday";
weekday[6]="Saturday";

var n = weekday[d.getDay()];
    
    
	var container = $("#container");
	
	// Shuffle the contents of container
	container.shuffleLetters();
 

	// Leave a 4 second pause

	setTimeout(function(){
		
		// Shuffle the container with custom text
		container.shuffleLetters({
			"text": "It's "+n+"!"
		});
		 		
	},4000);
	
});

