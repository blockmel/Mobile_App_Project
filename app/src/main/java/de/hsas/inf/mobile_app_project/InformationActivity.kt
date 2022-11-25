package de.hsas.inf.mobile_app_project

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InformationActivity : AppCompatActivity() {
    val INFO_ACT_KEY = "InformationActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        Log.e(INFO_ACT_KEY, "Information Activity created")
        val place_id: TextView = findViewById(R.id.place_id)
        val text = "ID: " + intent.getStringExtra("ID")+
                "\nLocation: " + intent.getStringExtra("location") +
                "\nName: " + intent.getStringExtra("name") +
                "\nGaelic name: " + intent.getStringExtra("gaelic_name") +
                "\nPlace type ID: " + intent.getStringExtra("placeTypeID") +
                "\nPlace type name: " + intent.getStringExtra("placeTypeName") +
                "\nPlace type created at: " + intent.getStringExtra("placeTypeCreated") +
                "\nPlace type updated at: " + intent.getStringExtra("placeTypeUpdated") +
                "\nLatitude: " + intent.getStringExtra("latitude") +
                "\nLongitude: " + intent.getStringExtra("longitude")
        place_id.setText(text)



    }
}