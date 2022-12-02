package de.hsas.inf.mobile_app_project

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import de.hsas.inf.mobile_app_project.dataTypes.PlaceTypes
import de.hsas.inf.mobile_app_project.dataTypes.Places
import de.hsas.inf.mobile_app_project.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
    GoogleMap.OnMarkerDragListener, AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    val THIRD_ACT_KEY = "ThirdActivity"
    var places: Array<Places> = emptyArray()
    var placeTypes: Array<PlaceTypes> = emptyArray()
    var markersAdded: Boolean = false
    lateinit var gm: GoogleMap
    lateinit var circle: Circle
    lateinit var slider: Slider
    val touchListener: Slider.OnSliderTouchListener = object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) {
            val text = "start " + slider.value.toString()
            Log.e(THIRD_ACT_KEY, text)
        }

        override fun onStopTrackingTouch(slider: Slider) {
            val texten = "stop " + slider.value.toString()
            Log.e(THIRD_ACT_KEY, texten)

            val latitude = circle.getCenter().latitude
            val longitude = circle.getCenter().longitude

            val loc1 = Location("")
            loc1.setLatitude(latitude)
            loc1.setLongitude(longitude)
            val loc2 = Location("")
            var distance: Float = 1.0E19F
            places.forEach {
                loc2.setLatitude(it.latitude)
                loc2.setLongitude(it.longitude)
                val distanceInMeters: Float = loc1.distanceTo(loc2)
                val distanceInKilometers: Float = distanceInMeters/1000
                if (distanceInKilometers < distance){
                    distance = distanceInKilometers
                }
            }
            circle.remove()
            circle = gm.addCircle(
                CircleOptions()
                    .center(LatLng(latitude, longitude))
                    .radius(slider.value.toDouble()*1000)
                    .fillColor(0x5500a2ff)
                    .strokeWidth(0F)
            )

            //https://stackoverflow.com/questions/16082622/check-if-marker-is-inside-circle-radius
            val dist = FloatArray(2)
            var count = 0

            places.forEach {
                Location.distanceBetween(
                    it.latitude, it.longitude,
                    circle.center.latitude, circle.center.longitude, dist
                )

                if (dist[0] <= circle.radius) {
                    count++
                }
            }

            val text = "Distance to the nearest place: " + distance.toString() + "\nNumber of places in the radius: " + count.toString()

            val snackBar =
                Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG)
            snackBar.show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

        slider = findViewById(R.id.slider)
        slider.addOnSliderTouchListener(touchListener)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.main_fragment, MainFragment().apply {
                arguments = Bundle().apply {

                }
            })
        }

        val client = OkHttpClient()
        var request = Request.Builder().url("https://gist.githubusercontent.com/saravanabalagi/541a511eb71c366e0bf3eecbee2dab0a/raw/bb1529d2e5b71fd06760cb030d6e15d6d56c34b3/places.json").build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(THIRD_ACT_KEY, "Exception: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val bodyString = response.body?.string()
                    val gson = Gson()
                    places = gson.fromJson(bodyString, Array<Places>::class.java)
                    places.forEach { Log.i(THIRD_ACT_KEY, it.toString()) }
                    Handler(Looper.getMainLooper()).post {
                        addMarkers()
                    }

                }
            }
        })

        request = Request.Builder().url("https://gist.githubusercontent.com/saravanabalagi/541a511eb71c366e0bf3eecbee2dab0a/raw/bb1529d2e5b71fd06760cb030d6e15d6d56c34b3/place_types.json").build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(THIRD_ACT_KEY, "Exception: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val bodyString = response.body?.string()
                    val gson = Gson()
                    placeTypes = gson.fromJson(bodyString, Array<PlaceTypes>::class.java)
                    placeTypes.forEach { Log.i(THIRD_ACT_KEY, it.toString()) }
                }
            }
        })

        val spinner: Spinner = findViewById(R.id.place_type_spinner)
        spinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            this,
            R.array.place_types_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    fun addMarkers() {
        if (gm == null) return
        gm.clear()
        places.forEach {
            val placeCoordinates = LatLng(it.latitude, it.longitude)
            gm.addMarker(
                MarkerOptions()
                    .position(placeCoordinates)
                    .title(it.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(getColor(it))))
        }
        markersAdded = true
    }

    override fun onMapReady(gmLocal: GoogleMap) {
        gm = gmLocal
        gm.uiSettings.isZoomControlsEnabled = true
        if (!markersAdded) addMarkers()
        with(gm){
            setOnInfoWindowClickListener(this@MainActivity)
            setOnMarkerDragListener(this@MainActivity)
        }
        gm.setOnMapLongClickListener { latLng ->
            val loc1 = Location("")
            loc1.setLatitude(latLng.latitude)
            loc1.setLongitude(latLng.longitude)
            val loc2 = Location("")
            var distance: Float = 1.0E19F
            places.forEach {
                loc2.setLatitude(it.latitude)
                loc2.setLongitude(it.longitude)
                val distanceInMeters: Float = loc1.distanceTo(loc2)
                val distanceInKilometers: Float = distanceInMeters/1000
                if (distanceInKilometers < distance){
                    distance = distanceInKilometers
                }
            }
            gm.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .draggable(true)
            )

            slider.value = 10F
            circle = gm.addCircle(
                CircleOptions()
                    .center(latLng)
                    .radius(10000.0)
                    .fillColor(0x5500a2ff)
                    .strokeWidth(0F)
            )

            slider.visibility = View.VISIBLE

            //https://stackoverflow.com/questions/16082622/check-if-marker-is-inside-circle-radius
            val dist = FloatArray(2)
            var count = 0

            places.forEach {
                Location.distanceBetween(
                    it.latitude, it.longitude,
                    circle.center.latitude, circle.center.longitude, dist
                )

                if (dist[0] <= circle.radius) {
                    count++
                }
            }

            val text = "Distance to the nearest place: " + distance.toString() + "\nNumber of places in the radius: " + count.toString()

            val snackBar =
                Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG)
            snackBar.show()

        }
    }

    fun getColor(it: Places): Float {
        when(it.place_type_id){
            1 -> return 0F
            2 -> return 22F
            3 -> return 44F
            4 -> return 66F
            5 -> return 88F
            6 -> return 110F
            7 -> return 150F
            8 -> return 164F
            9 -> return 186F
            10 -> return 208F
            11 -> return 230F
            12 -> return 260F
            13 -> return 274F
            14 -> return 296F
            15 -> return 328F
            else -> {
                Log.e(THIRD_ACT_KEY,"No place type")
                return 340F
            }
        }
    }

    override fun onInfoWindowClick(marker: Marker) {
        val intent= Intent(this, InformationActivity::class.java)
        Log.e(THIRD_ACT_KEY, marker.toString())
        Log.e(THIRD_ACT_KEY, marker.id)
        Log.e(THIRD_ACT_KEY, marker.title.toString())
        Log.e(THIRD_ACT_KEY, marker.position.latitude.toString())
        Log.e(THIRD_ACT_KEY, marker.position.longitude.toString())

        var placeFound = false
        places.forEach {
            if (!placeFound && marker.position.latitude == it.latitude && marker.position.longitude == it.longitude &&
                marker.title.toString() == it.name){
                val place = places.get(it.id - 1)
                Log.e(THIRD_ACT_KEY, "here "+place.id)
                intent.putExtra("ID", place.id.toString())
                intent.putExtra("location", place.location)
                intent.putExtra("name", place.name)
                if(place.gaelic_name == null) {
                    intent.putExtra("gaelic_name", "null")
                } else {
                    intent.putExtra("gaelic_name", place.gaelic_name)
                }
                val placeType = placeTypes.get(place.place_type_id-1)
                intent.putExtra("placeTypeID", placeType.id.toString())
                intent.putExtra("placeTypeName", placeType.name)
                intent.putExtra("placeTypeCreated", placeType.created_at)
                intent.putExtra("placeTypeUpdated", placeType.updated_at)
                intent.putExtra("latitude", place.latitude.toString())
                intent.putExtra("longitude", place.longitude.toString())

                placeFound = true
                startActivity(intent)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (gm == null) return
        gm.clear()
        if (pos == 0) {
            places.forEach {
                val placeCoordinates = LatLng(it.latitude, it.longitude)
                gm.addMarker(
                    MarkerOptions()
                        .position(placeCoordinates)
                        .title(it.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(getColor(it))))
            }
        } else {
            places.forEach {
                if(it.place_type_id == pos) {
                    val placeCoordinates = LatLng(it.latitude, it.longitude)
                    gm.addMarker(
                        MarkerOptions()
                            .position(placeCoordinates)
                            .title(it.name)
                            .icon(BitmapDescriptorFactory.defaultMarker(getColor(it))))
                }
            }
        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        
    }

    //https://github.com/googlemaps/android-samples/blob/main/ApiDemos/kotlin/app/src/gms/java/com/example/kotlindemos/MarkerDemoActivity.kt
    override fun onMarkerDrag(marker: Marker) {
        val loc1 = Location("")
        loc1.setLatitude(marker.position.latitude)
        loc1.setLongitude(marker.position.longitude)
        val loc2 = Location("")
        var distance: Float = 1.0E19F
        places.forEach {
            loc2.setLatitude(it.latitude)
            loc2.setLongitude(it.longitude)
            val distanceInMeters: Float = loc1.distanceTo(loc2)
            val distanceInKilometers: Float = distanceInMeters/1000
            if (distanceInKilometers < distance){
                distance = distanceInKilometers
            }
        }
        circle.remove()
        circle = gm.addCircle(
            CircleOptions()
                .center(LatLng(marker.position.latitude, marker.position.longitude))
                .radius(slider.value.toDouble()*1000)
                .fillColor(0x5500a2ff)
                .strokeWidth(0F)
        )

        //https://stackoverflow.com/questions/16082622/check-if-marker-is-inside-circle-radius
        val dist = FloatArray(2)
        var count = 0

        places.forEach {
            Location.distanceBetween(
                it.latitude, it.longitude,
                circle.center.latitude, circle.center.longitude, dist
            )

            if (dist[0] <= circle.radius) {
                count++
            }
        }

        val text = "Distance to the nearest place: " + distance.toString() + "\nNumber of places in the radius: " + count.toString()

        val snackBar =
            Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG)
        snackBar.show()
    }

    override fun onMarkerDragEnd(p0: Marker) {}

    override fun onMarkerDragStart(p0: Marker) {}
}