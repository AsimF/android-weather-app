package com.example.android_weather_app

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import android.os.AsyncTask
import android.util.Log


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat:Double = 0.0
    private var lon:Double = 0.0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        update()
    }

    private fun update(){
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                if (location != null){
                        lat = location.latitude
                        lon = location.longitude

                        var str = URL("http://samples.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=b1608dd44f80ebacd6e4b7ea11bb6cae").readText()
                        Log.d("retrieves", str);
//                        val url1 = URL("http://samples.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=b1608dd44f80ebacd6e4b7ea11bb6cae")
//                        val url2 = URL("")
//                        val url3 = URL("")
//                        val url1Connection = url1.openConnection() as HttpURLConnection
//                        val url2Connection = url2.openConnection() as HttpURLConnection
//                        val url3Connection = url3.openConnection() as HttpURLConnection
//                        try {
//                            city_name.text = ""+url1Connection.responseMessage[0]
//                        } finally {
//                            url1Connection.disconnect()
//                            url2Connection.disconnect()
//                            url3Connection.disconnect()
//                        }
                    //city_name.text = "still here"
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
