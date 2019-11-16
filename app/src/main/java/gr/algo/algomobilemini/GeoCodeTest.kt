package gr.algo.algomobilemini

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log

import kotlinx.android.synthetic.main.activity_geo_code_test.*
import java.io.IOException

class GeoCodeTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geo_code_test)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val latitude: Double
            val longitude: Double

            var geocodeMatches: List<Address>? = null

            try {
                geocodeMatches = Geocoder(this).getFromLocationName(
                        "Ομονοίας 26 Καβάλα", 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (geocodeMatches != null) {
                latitude = geocodeMatches[0].latitude
                longitude = geocodeMatches[0].longitude
                Log.d("JIM-lat",latitude.toString())
                Log.d("JIM-long",longitude.toString())
            }
        }
    }

}
