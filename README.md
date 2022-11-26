# Mobile_App_Project

## Course Information
- Module Title:Mobile Applications 2
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
    - [x] Show the image of the place if available, else, show a random image loaded from the internet 
    - [x] Show a back button, when pressed, go back to map view
- 3. Allow filtering by Place Type 
    - [x] Show dropdown for selecting Place Type, default value should be all
    - [x] When a specific Place Type is selected, show only the places with this Place Type on the map
- 4. Allow custom marker
    - [x] Long Press anywhere on map to show a draggable marker
    - [x] Show distance to the nearest place (from the downloaded data), and on drag marker, update this info
    - [x] Draw a semi-transparent blue circle around this marker with a radius of 10 km and show number of places within this radius
    - [ ] Create a slider to control the above radius in km (1-1000, default 10) and on change, update the circle on the map and update number of places within the radius info shown accordingly

## Report


## References
From the following references code was used in part or fully:
- https://github.com/saravanabalagi/dorset_mobileApp/commit/b9af07fa9cfe9378eb1cbe2c121c6c52839036c5
- https://stackoverflow.com/questions/44239869/whats-the-kotlin-equivalent-of-javas-string
- https://stackoverflow.com/questions/45685026/how-can-i-get-a-random-number-in-kotlin
- https://github.com/googlemaps/android-samples/blob/main/ApiDemos/kotlin/app/src/gms/java/com/example/kotlindemos/MarkerDemoActivity.kt
- http://wptrafficanalyzer.in/blog/semi-transparent-color-for-fillcolor-in-google-maps-android-api-v2/
- https://stackoverflow.com/questions/16082622/check-if-marker-is-inside-circle-radius
