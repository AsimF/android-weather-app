package com.example.android_weather_app

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
        //refresh_button.setOnClickListener {
        //    update()
        //}
        update()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    private fun update() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                lat = location.latitude
                lon = location.longitude
                val url1 = "https://api.darksky.net/forecast/bda8906c093400944402ffa53135348d/${lat},${lon}"
                Log.d("Before API Call", result.toString())
                //city_name.text = URL(url1).readText().toString()
//                try {
//                    //result = URL(url1).readText(Charsets.UTF_16)
//                    city_name.text = URL(url1).readText()//result.toString()
//                    Log.d("reached the call", "made it")
//                } catch (ex : Exception){
//                    ex.printStackTrace()
//                } finally {
//                    Log.d("After API Call", result.toString())
//                }
            } else {
                //city_name.text = "ERROR! Location services not enabled!"
            }
        }
    }

    private fun updateCurrent(){

    }

    private fun updateHourly(){

    }

    private fun updateDaily(){

    }

}
