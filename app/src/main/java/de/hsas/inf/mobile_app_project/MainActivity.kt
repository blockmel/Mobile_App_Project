package de.hsas.inf.mobile_app_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.commit
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.*
import java.io.IOException
import com.google.gson.Gson
import de.hsas.inf.mobile_app_project.dataTypes.PlaceTypes
import de.hsas.inf.mobile_app_project.dataTypes.Places
import de.hsas.inf.mobile_app_project.databinding.ActivityMainBinding



/*val colors = mapOf(1 to 0F, 2 to 22F, 3 to 44F, 4 to 66F, 5 to 88F, 6 to 110F, 7 to 150F,
    8 to 164F, 9 to 186F, 10 to 208F, 11 to 230F, 12 to 260F, 13 to 274F, 14 to 296F, 15 to 328F)*/

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    val THIRD_ACT_KEY = "ThirdActivity"
    var places: Array<Places> = emptyArray()
    var placeTypes: Array<PlaceTypes> = emptyArray()
    var markersAdded: Boolean = false
    lateinit var gm: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

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
        /*client.newCall(request).execute()
        try(Response response = client.newCall(request).execute()){

        } */
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
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.place_types_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
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
            //.icon(BitmapDescriptorFactory.defaultMarker(colors.get(it.placeTypeId))))
        }
        markersAdded = true
    }

    override fun onMapReady(gmLocal: GoogleMap) {
        gm = gmLocal
        gm.uiSettings.isZoomControlsEnabled = true
        if (!markersAdded) addMarkers()
        with(gm){
            setOnInfoWindowClickListener(this@MainActivity)
        }
       /*gm.moveCamera(CameraUpdateFactory.newLatLng(LatLng(53.4, -6.3)))*/
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
}