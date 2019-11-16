package gr.algo.algomobilemini

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var latitude:Double=0.00
    var longitude:Double=0.00
    var routeId=0
    lateinit var place:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setTitle("Xάρτης περιοχής")
        latitude=intent.extras.getDouble("latitude")
        longitude=intent.extras.getDouble("longitude")
        place=intent.extras.getString("place")
        routeId=intent.extras.getInt("routeid")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }



    override fun onBackPressed() {
        super.onBackPressed()

        val i= Intent(this,CustomerListActivity::class.java)
        i.putExtra("routeid",routeId)
        startActivity(i)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val location = LatLng(latitude,longitude)
        //val sydney = LatLng(40.936993, 151.0)
        mMap.addMarker(MarkerOptions().position(location).title(place))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15.0f))
    }
}
