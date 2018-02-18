An interactive Fintech Application 
An Android Application that shows basic implementation of an app like Paytm or Anyother ecommerce application 
The app fully functional with use of server backend hosted at Heroku, I will explain the server details in the later part  
It includes all the features stated in problem statement and some additional features too.  
 To see the demo of the application see the video  
 One can Install the app using apk in mobile phone, best made for phones having API 25  
 # Functioning  
 1. Signup using basic information
 2. Sign in with username and password which you created during signup process  
 3. One can see few offers along with a transfer money and recharge button.  
 4. Firstly one needs to complete further registration by adding Credit Card details and Profile.
 5 navigation of both is on the downside of the app window.
 6. After completing both profile and credit card, one can do task like recharge, buy from the offers, transfer money to someone  
 7. All these features have working logic along with server implementation too.  
 8. For example, when one buys something from main offer page, its account balance gets reduced by that much amount and he gets some redeem point too according to the problem statement  
 which can be seen in the wallet section of the app  
 9. Similarly, other features include review of transactions done, monthly insights from the transactions etc  
 10. The project consists of both a web app and a mobile app. Web App is just an API based APP written in Django and Hosted on Heroku and Mobile app consumes API services like authentication,   
 fetching of data, handling every other feature of the application.  

# username - guest  
# password - guest  
 - 20:--IBM Watson -- used IBM Watson visual recognition service for detection of the face while uploading the profile pic, its avoids and restricts the user from uploading a random pic, only a pic with face in it can be uploaded.  
To test this feature one can go to profile section of the app and click on the photo on the upper left corner of the phone screen.  
Click on the area of pic to open camera and upload your photo, the app relaunches camera if you click any other photo, which is not having any  
face it, the demo is shown at this URL https://youtu.be/1RbhwEQB7DY


# EndNote  
Installing APK-- https://github.com/dominicneeraj/KardPay/blob/master/app-release.apk 
Web API URL--  https://kradapi.herokuapp.com/admin/
Web API source code -- https://github.com/dominicneeraj/KardPay-Api
Demo video1--  https://youtu.be/ilZpAZkCBOI
Demo Video2(implementation of Watson Face detection)  -- https://youtu.be/1RbhwEQB7DY

# some Screens
login screen  
![screen12](https://user-images.githubusercontent.com/17751493/30934746-19bb8310-a3ec-11e7-8402-b575e25a5d6f.png)

signup screen
![screen13](https://user-images.githubusercontent.com/17751493/30934856-7e8f7292-a3ec-11e7-9450-ee2df60f2364.png)

Home screens

![screen1](https://user-images.githubusercontent.com/17751493/30934881-90ab4cda-a3ec-11e7-8f52-e09f1e52068a.png)
![screen2](https://user-images.githubusercontent.com/17751493/30934894-9fe9217c-a3ec-11e7-904c-3f11f7d5ed1e.png)
![screen3](https://user-images.githubusercontent.com/17751493/30934913-af28a54a-a3ec-11e7-80b1-0934a01afd81.png)
![screen4](https://user-images.githubusercontent.com/17751493/30934942-c3dda56c-a3ec-11e7-8f66-fb40fd79d7c8.png)
![screen5](https://user-images.githubusercontent.com/17751493/30934963-d16b4252-a3ec-11e7-9f56-841aad75ebb5.png)
![screen6](https://user-images.githubusercontent.com/17751493/30934977-dd30c756-a3ec-11e7-8634-4ff75275f3a4.png)
![screen7](https://user-images.githubusercontent.com/17751493/30934980-e19fa582-a3ec-11e7-9c95-507085140bfb.png)
![screen8](https://user-images.githubusercontent.com/17751493/30934983-e49c0884-a3ec-11e7-8e16-e836ebfb947b.png)
![screen9](https://user-images.githubusercontent.com/17751493/30934986-e7fad4ce-a3ec-11e7-86a5-1c9a1628281f.png)
![screen10](https://user-images.githubusercontent.com/17751493/30934988-ea5e4732-a3ec-11e7-816e-9e4801516f62.png)
![screen11](https://user-images.githubusercontent.com/17751493/30934991-eca6618c-a3ec-11e7-9769-1c26ba7aedc5.png)

