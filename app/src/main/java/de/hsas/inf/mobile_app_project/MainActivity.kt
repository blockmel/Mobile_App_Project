package de.hsas.inf.mobile_app_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.hsas.inf.mobile_app_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
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
    }

    override fun onMapReady(gm: GoogleMap) {
        val dublin = LatLng(53.4, -6.3)
        gm.uiSettings.isZoomControlsEnabled = true
        gm.addMarker(
            MarkerOptions()
                .position(dublin)
                .title("Marker in Dublin"))
        gm.moveCamera(CameraUpdateFactory.newLatLng(dublin))
    }
}