package de.hsas.inf.mobile_app_project

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class InformationActivity : AppCompatActivity()  {
    val INFO_ACT_KEY = "InformationActivity"
    //https://stackoverflow.com/questions/44239869/whats-the-kotlin-equivalent-of-javas-string
    val urlArray = arrayOf("https://cdn.pixabay.com/photo/2022/11/12/09/25/mountains-7586568_1280.jpg",
        "https://cdn.pixabay.com/photo/2022/10/29/07/15/ocean-7554578_1280.jpg",
        "https://cdn.pixabay.com/photo/2022/11/14/10/54/sunrise-7591335_1280.jpg",
        "https://cdn.pixabay.com/photo/2022/11/20/20/43/fall-7605210_1280.jpg",
        "https://cdn.pixabay.com/photo/2022/10/17/17/37/sea-7528351_1280.jpg")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        Log.e(INFO_ACT_KEY, "Information Activity created")
        val place_id: TextView = findViewById(R.id.place_id)
        val text = "ID: " + intent.getStringExtra("ID") +
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

        val imageView = findViewById<ImageView>(R.id.image)
        //https://stackoverflow.com/questions/45685026/how-can-i-get-a-random-number-in-kotlin
        val number = (0..4).random()
        val dpUrl = urlArray.get(number)
        Picasso.get()
            .load(dpUrl)
            .into(imageView)

        val button: Button = findViewById(R.id.back_button)
        button.setOnClickListener {
            this.finish()
        }
    }
}