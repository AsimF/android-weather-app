package com.example.android_weather_app

import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import java.net.URL
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat:Double = 0.0
    private var lon:Double = 0.0
    private var result: Any? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        parent_layout_id.setOnClickListener {
            window.decorView.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        refresh_id.setOnClickListener {
            update()
        }
        update()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    private fun update() {
        doAsync {
            var lat = 36.3504
            var lon = 127.3845
            city_name_id.text="waiting for api call"
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                } else {

                }
            }
            //city_name_id.text = URL("https://api.darksky.net/forecast/bda8906c093400944402ffa53135348d/${lat},${lon}").readText()
            val geocoder =
                Geocoder(this@MainActivity, Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(lat, lon, 1)
            val cityName: String = addresses[0].locality
            city_name_id.text = cityName
        }
    }

    private fun updateCurrent(){

    }

    private fun updateHourly(){

    }

    private fun updateDaily(){

    }

}
