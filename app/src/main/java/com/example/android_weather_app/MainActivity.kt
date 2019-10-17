package com.example.android_weather_app

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import android.os.AsyncTask
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat:Double = 0.0
    private var lon:Double = 0.0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        refresh_button.setOnClickListener {
            update()
        }
        update()
    }

    private fun update(){
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                if (location != null){
                    lat = location.latitude
                    lon = location.longitude
                    val url1 = "http://samples.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=b1608dd44f80ebacd6e4b7ea11bb6cae"
                    AsyncRequest().execute(url1)
//                    val url2 = URL("")
//                    val url3 = URL("")
//                    val url1Connection = url1.openConnection() as HttpURLConnection
//                    val url2Connection = url2.openConnection() as HttpURLConnection
//                    val url3Connection = url3.openConnection() as HttpURLConnection
//                    try {
//                        city_name.text = ""+url1Connection.responseMessage[0]
//                    } finally {
//                        url1Connection.disconnect()
//                        url2Connection.disconnect()
//                        url3Connection.disconnect()
//                    }
                    //city_name.text = "still here"
                    } else {
                    //city_name.text = "ERROR! Location services not enabled!"
                    }
            }
    }

    inner class AsyncRequest : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String): String {
            var text: String = ""
            var connection = URL(url[0]).openConnection() as HttpURLConnection
            //var connection = URL("android.com") as HttpURLConnection

            try {
                //connection.connect()
                //text = connection.inputStream.use { it.reader().use{reader -> reader.readText()} }
            } finally {
                //connection.disconnect()
            }
            return text
        }

        override fun onPostExecute(result: String) {
            //super.onPostExecute(result)
            //jsonParse(result)
        }
    }

    private fun jsonParse(jsonString: String) {
//        val jsonArray = JSONArray(jsonString)
//        val list = ArrayList<Forecast>()
//        var x = 0
//        while (x < jsonArray.length()){
//            val jsonObject = jsonArray.getJSONObject(x)
//            list.add(Forecast(
//                    jsonObject.getDouble("temp"),
//                    jsonObject.getDouble("temp_max"),
//                    jsonObject.getDouble("temp_min"),
//                    jsonObject.getDouble("humidity")
//            ))
//            x++
//        }
    }

    private fun updateCurrent(){

    }

    private fun updateHourly(){

    }

    private fun updateDaily(){

    }

}
