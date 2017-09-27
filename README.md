# KardPay
A interactive application for Credit card holders  
An android Application that makes Credit card Maintainance Easy  
The APP has been developed as a part of problem statement given by Codefest, IIT BHU.  
The app is fully functions with use of server backend hosted at IBM Bluemix, I will explain the server details in the later part  
It includes all the features stated in problem statement and some additional features too.  
 To see the demo of the application see the video  
 One can Install the app using apk in mobile phone, best made for phones having API 25  
 #Functioning  
 1. Signup using basic information
 2. Signin with username and password which you created uring signup process  
 3. One can see few offers alongwith a transfer money and recharge button.  
 4. Firstly one need to complete further registration by adding Credit Card details and Profile.
 5 navigation of both is on the downside of the app window.
 6. After completing both profile and credit card, one can do task like recharge, buy from the offers, transfer money to someone  
 7. All thses features have working logic along with server implementation too.  
 8. For example when one buys something from main offer page, its account balance gets reduced by that much amount and he gets some redeem point too according to the problem statement  
 which can be seen in the wallet section of the app  
 9. Similarly other features include review of trnsactions done, monthly insights from the transactions etc  
 
 #Browny points
 Features Included for the claim of browny points as mentioned in the problem statement.  
 The project consists of both a web app and a mobile app. Web App is just an api based APP written in django and Hosted on Bluemix and Mobile app consumes API services like authentication,   
 fetching of data, handling every other feature of the application.  
 - 10: Accessing SD Card contents - 10: Camera  -- used for uploading profile photo in profile section  
 - 10: Navigation Drawer  -- Can be accessed by tapping hamburger icon in the upper left corner of the screen  
 - 15: Social login (Custom login)  -- custom athentication system made using API based Oauth2 token( API developed in django and hosted on bluemix) 
 - 15: Using Internet API calls  -- Heavy use of API calls inside the mobile application, all the functions of application, logically implemented  
                                    using API calls from internet including(authentication, registration, fetching data like transaction details  
                                    offers , adding credit card, profile updating, profile pic uploading from api  
                                    
 --WebApp  -- Deveoped in Django a database backed REST API service for the mobile app  
 --IBM Bluemix  hosted on IBM bluemix with url http://kradapi-semimountainous-bachelorhood.mybluemix.net/admin/  
 for guset user, to test backend of mobile application, Please use following credentials
#username - guest  
#password - guest  
--IBM Watson -- used IBM watson visual recognition service for detection of face while uploading the profile pic, its avoids and restricts user from uploading random pic, only a pic with face in it can be uploaded.  
To test this feature one can go to profile section of the app and click on photo on the upper left corner of the phone screen.  
Click on the area of pic to open camera and upload your photo, the app relaunches camera if you click any other photo, which is not having any  
face in it, demo is shown at this url.


#End Note  
Installing APK--  
Web API url--  
Demo video1--  
Demo Video2(implementation of Watson Face detection)  --

#some Screens
