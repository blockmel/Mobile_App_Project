# Mobile_App_Project

## Course Information
- Module Title: Mobile Applications 2
- Lecturer name: Saravanabalagi Ramachandran

## Student Information
- Name: Melanie Block
- Student ID: 25976

## Requirements Checklist
- 1. Display markers on the map
    - [x] Load places and place types JSON data
    - [x] Display marker for all places using GPS coordinates
    - [x] Use different colours, one for each place type
    - [x] When marker is tapped, place name should be displayed on a pop-up info window
- 2. Show extended information
    - [x] When the pop up info window is tapped, show in full screen all details of the place (id, name, Gaelic name, type, GPS coordinates)
    - [x] Show the image of the place loaded from the internet
    - [ ] [Bonus] Show relevant image using any free web service, or search engine
    - [x] Show a back button, when pressed, go back to map view
- 3. Allow filtering by Place Type 
    - [x] Show dropdown for selecting Place Type, default value should be all
    - [x] When a specific Place Type is selected, show only the places with this Place Type on the map
- 4. Allow custom marker
    - [x] Long Press anywhere on map to show a draggable marker
    - [x] Show distance to the nearest place (from the downloaded data), and on drag marker, update this info
    - [x] Draw a semi-transparent blue circle around this marker with a radius of 10 km and show number of places within this radius
    - [x] [Bonus] Create a slider to control the above radius in km (1-1000, default 10) and on change, update the circle on the map and update number of places within the radius info shown accordingly

## Report
During the process of creating the app I faced some challenges.  
  
My first challenge was that I had problems with reading the data from the JSON-file because of a bug in Android Studio. Therefor Android Studio marked “_” in parameter names as an error, even if they had to be there, so that the data could get read.  
  
My second challenge was getting the data from the MainActivity to the InformationActivity. Here I remembered that there is something named “intent” in Java. So I search for this in Kotlin and for the exact usage. With the help of the documentation I could fix this problem and also the problem, that the data I wanted to get to the InformationActivity had different data types.  
  
My third challenge was to get an onClick-action for a button to go back to the map view. The problem here was, that, the code example in the Kotlin-documentation was in Java. With the help of some other documentations, like the ones from Buttons and View.OnClickListener, I could solve this probleme.  
  
The next challenge was loading random images from the internet over the URL in an ImageView. Here the code on GitHub from Mobile App 1 helped me. Through this code I used the library Picasso.  
  
In context with loading the images I faced another challenge, as I had to find out how a string-array in Kotlin works. The documentation about this topic was not useful, so I search on stackoverflow, where I found some good explanations.  
  
The last challenge was in context with the dropdown menu. After I selected a specific Place Type there were problems with showing all details of the place in full screen. The number of the id of the markers got incremented, in the form that the number of the now selected markers got added to the number of makers that already had been shown before. So, it was not possible anymore to get the place over the marker id. I solved the problem by searching now for the place with the langitude, the longitude and the name of the marker.  
  
During the process of creating the app I could learn a lot about the usage of Kotlin, the different possibilities it supports and the differences to Java.


## References
From the following references code was used in part or fully:
- https://github.com/saravanabalagi/dorset_mobileApp/commit/b9af07fa9cfe9378eb1cbe2c121c6c52839036c5
- https://github.com/saravanabalagi/dorset_mobileApp2
- https://stackoverflow.com/questions/44239869/whats-the-kotlin-equivalent-of-javas-string
- https://stackoverflow.com/questions/45685026/how-can-i-get-a-random-number-in-kotlin
- https://github.com/googlemaps/android-samples/blob/main/ApiDemos/kotlin/app/src/gms/java/com/example/kotlindemos/MarkerDemoActivity.kt
- http://wptrafficanalyzer.in/blog/semi-transparent-color-for-fillcolor-in-google-maps-android-api-v2/
- https://stackoverflow.com/questions/16082622/check-if-marker-is-inside-circle-radius
- https://stackoverflow.com/questions/62409442/how-to-get-value-from-new-android-material-design-slider
