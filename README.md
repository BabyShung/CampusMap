CampusMap
=========

This app is for University of Iowa.

Demo:
http://www.youtube.com/watch?v=9BYGYzdonMU


ABSTRACT

CampusGPS, a different perspective of navigation
 

With the increase of buildings in a university, sometimes it becomes difficult to get directions to different campus buildings. Though Google provides navigation among places, it doesn’t always return the most reasonable route or get adapted to recent changes that happened around buildings. Therefore, it is significant to render optimized paths that are more practical to students on campus. The idea is pretty straightforward, users share their path around the campus and then the campus GPS server receives these routes. Next time when other users request direction to a campus building, previous routes will be reused. Another reason for developing this app is to better enhance time management for students. A couple of paths to a building are provided whenever requested by users. User can make comparisons among these server-returned paths and choose their favorite. Better user interface and friendly user experience also complete the aim of this app.

1.	INTRODUCTION
This application is designed to provide better direction paths based on algorithms that would choose the best according to influencing factors like shortest distance, less time, more scenic, better snow path, etc. For the time being, only two factors are considered, distance and time. A Client-Server model is applied since route data is shared anonymously between different users. The client here is represented as smart phones interacting with a server. It can perform upload or retrieval of paths. System interaction and process will be explained in details as we go further.
2.	SYSTEM DESCRIPTION
Typically, the system interaction diagram is shown as follows.

As we can see from the above diagram, the smart phone detects its location using GPS. Current location is sent to the server along with the destination location when route requesting is performed. Users can either select a destination from a predefined list of buildings or can tap on the buildings to request routes. Once users get the returned routes (which is usually 3 routes), they can choose the one they want and start walking at their wish. Then users’ paths will start to be recorded. After arriving at the destination, when users click stop, the recorded route will be uploaded to the server whenever the Internet connection is available. Next time if the routes are requested, the server will analyze possible usable paths and return a couple of best routes according to the algorithm formulated. 
2.1	User Interface design
We believe that for the user interface: the simpler the better. Minimal number of building block is arranged in the relative layout so that our app can display according to different size of devices. The colors used in our app are basically gold and black, which reflects the theme of University of Iowa. 
Based on requirement analysis, our design has five activities. In order to let users have better user experience, choosing better and simpler styles, patterns, building blocks for each activity were applied. 

•	MainActivity:
In this activity, we use a TabHost which holds other 4 activities. TabHost has two components: one is a set of tab labels that the user can click to select a specific activity; another is a FrameLayout object that displays the contents of that activity. This is a popular design style and is very easy to navigate between activities for the user.

•	MapActivity: 
Map activity contains a map fragment where is used to load the Google map. We customize the Google map in many ways to fulfill our functional goals and give the user better user experience. Recoloring the campus buildings, adding markers to show building information, setting up different OnclickListeners to receive the user’s action are implemented.

•	SearchActivity:
Search activity consists of many building rows. We wanted to render  them in a simpler and more friendly way. AutoCompleteTextView and a Button are applied to fulfill the searching function. Also,  ListView is used to display the building information stored in our database. The adapter for the ListView is to populate multi-items in one line including building icons, names and requested time.

•	RouteActivity:
Route activity is to populate user’s routes in a ListView. It is similar to the search activity and we chose an elegant format to display considering the length of different data.

•	SettingActivity:
In the setting activity, we defined some basic features such as deleting the route history and deleting the query time in the search activity, etc.

3.	Optimization and algorithm
In this project, GPS plays a very indispensable role as it determines user’s current location and records the path to the destination building. The GPS signal we received doesn’t always provide accurate locations. Therefore it is meaningful for us to smoothen the recorded path in order to provide better accuracy. Actually, we used Kalman filter and Geometric Location estimate to complete our route-recording function. Additionally, we have a strategy for requesting the route, all of which will be explained below.
3.1	Using Kalman filter
Our route-recording algorithm works both indoors and outdoors. Outdoor GPS signal is ready for us to record. Since outdoors signals can be pretty unstable at random moment, we applied Kalman filter to smoothen our path. The Kalman filter does analyze the variance and time of every point (location) in a route. Let’s say during the recording the route got some unreasonable locations stored, the Kalman filter algorithm can “drag” those points back to the right track. This theory is sort of complex but do give us great help to perform relatively better recordings. 

3.2	Geometric Location estimate
Continue for route recording, indoors locating, however, is a rather complex challenge. There are a lot of topics aimed to implement indoor recording such as using WIFI Access Points and Sensor Fusion (combination of accelerometer, gyroscope and magnetic field device). Both the above two topics needs a lot of experiments and research, we did read a couple of paper and then decided to give them up. A third approach is done based on our mathematical knowledge. We called it Geometric location estimate. 

Geometric location estimate is sort of naïve, since it works only if the GPS signal is reliable. We already know that in a building, the GPS signal is terrible. Thus, if we have detected signal has been lost for five seconds, we will assert the user entered a specific building. So the next thing will be calculating which building our user has entered. And finally if a route has started, the center point of this building will be stored in this route.

Figure 2 shows how we calculate which building a user has entered. Let’s say the signal is lost for five seconds. Now we will construct several triangles in the nearby buildings, we calculate its distance based on the degree at current location’s closest point. If that degree is less than 90 degree, we will calculate the point to line distance. Otherwise, Euclidean distance to current location’s closest point will be calculated. More info is shown in the following pseudo-code (Experiments of this method were shown in the PPT).

3.3	Analyzing routes
Once we choose geometric location estimate and recorded 60 or so routes, the next step will be figuring out how to make use of our user-provided data and generate a more reasonable route. The approach at first seems to be really easy. For example, if I record a route from Main Library to Mclean Hall, then next time if a different student stands in the same position at library and request a route to Mclean, our server will just return this route back to the client. But it doesn’t work since locations are infinite and routes are limited. Thus, we calculate the range instead.

Our server essentially is looking for possible good routes and then sends them to the client. Before doing that, the server gets the client’s location and client’s destination. It then tries to find practical routes based on three different ranges: 20m, 40m, 60m within current location and 20m, 40m within destination. If the routes that satisfy these range combinations, the server will put it in a list. Finally the list will be returned (Our prototype currently returns 5 routes). Note that the returned routes can be considered raw routes to a requesting user. Thus our client needs another algorithm to modify and optimize these raw routes. 

The optimizing algorithm basically works with Google direction. When we send request to our own server, we also send request to Google direction. The Google direction returns a path (we guess that is a shortest path) back to the client. Our algorithm will detect whether the raw routes are crossed with Google path. If so, we will make use of part of the Google path from the beginning and continue draw our route. At the end of the route, we will also do a similar thing if the ending point is too far from the destination center. If so, we will connect the ending point of this recording route to the center point of a building. 

On the other hand, if our routes are not crossed with Google path, we simply find the closest point from those routes and connect my current location to the closest point and continue drawing (This method is rather naïve and needs to be improved).


Advantages:
The reason to choose this geometry method is because it takes less time to implement. We acknowledged that this estimate method is not very robust, but if we still want to do indoor recording, a simpler solution should be applied.
3.4	Reusing routes
Again, we state again that there are infinite locations on a map and the recorded routes are limited. How can we generate a new route if no routes are very close to my current location? Since we defined three types of range: 20m, 40m, 60m, we can sort of reuse other routes that are further but relatively closer. In figure 4 we can see that the red route is a generated route reusing a route from previous record. It makes use of part of the Google direction since the original raw route is crossed with Google path. The overlapping fraction is the part we need to generate a new one. For the remaining red route we still use them. In this case, we generated a shorter route than the Google direction, and it seems more reasonable for us to take this new route. More info is shown in the following pseudo-code (Experiments of this method were shown in the PPT)


3.5	Algorithm pseudo code
Algorithm 1 (Enter which building): 
•	Start GPS listeners
•	If signal is lost for five seconds, get the nearest four buildings to my current location.
•	For each of these four buildings:
a)    Find the closest two points to my current location (This will form a triangle).
b)    If the angle at my closest point is less than 90 degree, calculate the point (my location) to line distance.
c)    Else calculate the distance from my location to the closest point.
•	Compare these four distances, get the minimum distance and return the corresponding building.

Algorithm 2 (Route optimization): 
•	Receive the five routes from the server, and also get a route from Google direction.
•	For each of these five routes:
a)	First check if the route is crossed with Google direction.
b)    If so, generate a new route that starts from my location -> Google direction beginning -> the point that two routes crossed -> the remaining part of the user route.
c)    Else, calculate the closest point (also this point is not within a building – another algorithm) from the route to my location. Then generate a route that starts from my location -> closest point -> the remaining part of the route.
•	Next, check the ending point. If the point is 30 meters away, connect this point to the center point of destination.

4.	Evaluation
We measured our performance by considering the factors related to routes, coming from each path. A good route is associated with the accuracy of the GPS signal. After applying the Kalman filter, if this path has few bounce and looks pretty smooth, it could be considered as a good one. We recorded 60 routes, out of which 46 routes turned out to be good. Therefore, the performance of the system will be approximately 77%. But note that we were very cautious to do these experiments. We are not very sure what the outcome will be if more volunteers participate in our route recording.

4.1	Requesting routes
For now, if we request routes from a specific location to a building, it will at most return three routes. As we get more routes in our server database, it might lead another unforeseen research field such as how many routes do we need to saturate a university, or what criteria do we need for reusing the routes from the server. They are all very interesting topics, we believe. It is very obvious that the percentage of “Requesting three routes back” will increase when there are more routes on campus.

 

4.2	Good, Bad and Ugly
Out of 60 routes that we recorded, 46 turned out to be satisfactory. The rest of routes did not yield a better result, due to poor GPS signal. At first we have to delete some of them in our server database, since they were severely causing some problems such as users getting a pretty weird route. But we then modified our recording algorithm as well as optimizing algorithm. It seems that we can sort of tolerate some bad routes. We acknowledge that using only the GPS signal to do the recording is not reliable, let alone the indoor recording. From our perspective, we need to combine a better indoor recording approach such as WIFI access point or sensor fusion in the future to our app if we want our app to be used by different groups of students.

 

4.3	Tested VS Untested
It is important to know the location where our app works well. For that, we were able to test 34% (i.e 15 out of 45 buildings) and our app works fine within these buildings such as Main Library, Mclean Hall, Old Capitol Mall, etc. Unfortunately, we didn’t test all the buildings on campus and we are very sure that our app might have unforeseen bugs in those untested buildings.


5.	PROS & CONS
Advantages
•	The routes we get could be better than Google directions.
•	After optimizing, it can generate shorter routes
•	If the GPS signal is good, the route recording is perfect. 
•	Gives the user, an option to choose the route he/she wants to take along with the distance and time taken for each route by other users.
   Disadvantages
•	For now, the algorithm chooses the best route wrt time and distance factor.
•	If the GPS signal is very bad, there are bounces in the routes, despite using a smoothing algorithm.

6.	CONCLUSION
Our approach for generating a new route is definitely different from others. We never thought of that we would generate a route that can be based on Google. Usually, map path algorithms are used to generate a better route. However, we recorded our own path and then just do the optimization. Even though it can be considered naïve, it worked in a lot of cases. A future approach will be implement a better optimization algorithm, since we have our recorded routes. When different users share a lot of their own traces, we want to define a better algorithm that dissects their routes and generate a new, shorter, and even reasonable route.
Some of the enhancements can be made to the existing application. It could be to sync Google calendar to get alerts if you are near a building or share the shortest path on Facebook/Twitter to help your friends. This way, many people can get to know about our application. More the data, the better paths we get. Anyway, we are confident that this app is promising.
7.	REFERENCES
[1]	EasyTracker: Automatic Transit Tracking, Mapping, and Arrival Time Prediction Using Smartphones - James Biagioni - Department of Computer Science
University of Illinois at Chicago.


7.1.	Related websites

        http://www.eecs.harvard.edu/~konrad/projects/motetrack/
http://en.wikipedia.org/wiki/Kalman%5Ffilter
http://androidexample.com/Upload_File_To_Server_-_Android_Example/index.php?view=article_discription&aid=83&aaid=106
http://www.ahristov.com/tutorial/geometry-games/point-line-distance.html
http://www.vogella.com/articles/AndroidSensor/article.html
http://www.cs.uic.edu/~jakob/papers/easytracker-sensys11.pdf

http://www.thousand-thoughts.com/2012/03/android-sensor-fusion-tutorial/

http://stackoverflow.com/questions/3145089/what-is-the-simplest-and-most-robust-way-to-get-the-users-current-location-in-a/3145655#3145655

http://rvmiller.com/2013/05/part-1-wifi-based-trilateration-on-android/#comment-5922

http://ddewaele.github.io/GoogleMapsV2WithActionBarSherlock/part5

http://www.youtube.com/playlist?list=PL2F07DBCDCC01493A
http://www.androidviews.net/2013/05/httpsimonvt-github-iomessagebar/


