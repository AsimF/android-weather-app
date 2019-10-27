package com.example.android_weather_app

import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.*
import kotlin.math.round


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient //location provider
    private val api_key:String = "bda8906c093400944402ffa53135348d" //note we are using the DarkSky API - not the openweatherapi


    override fun onCreate(savedInstanceState: Bundle?) { //onCreate method - handle UI, otherwise use Async from anko
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        parent_layout_id.setOnClickListener { //get rid of the UI nav bar
            window.decorView.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this) //update button functionality
        refresh_id.setOnClickListener {
            update()
        }
        update() //update once
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) { //hide nav bar when refocusing
        super.onWindowFocusChanged(hasFocus)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    private fun update() {
        doAsync {
            var lat = 36.3504 //set base to Seoul
            var lon = 127.3845
            city_name_id.text="waiting for api call"
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude //get actual lat/lon of device
                    lon = location.longitude
                } else {
                    val builder = AlertDialog.Builder(this@MainActivity) //handle no location
                    builder.setTitle("location settings")
                    builder.setMessage("Please enable location settings for this app to work properly")
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }
            val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
            var addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1) //note these values may change based on loc
            var cityName: String = addresses[0].getAddressLine(0).split(",")[2]
            city_name_id.text = cityName //use for getting name

            //handle updating all the weather data below ~~~~~~
            var forecasts = URL("https://api.darksky.net/forecast/${api_key}/${lat},${lon}?exclude=minutely,alerts,flags&units=si").readText()
            var current:JSONObject = JSONObject(forecasts).optJSONObject("currently")
            var hourly:JSONArray = JSONObject(forecasts).optJSONObject("hourly").getJSONArray("data")
            var daily:JSONArray = JSONObject(forecasts).optJSONObject("daily").getJSONArray("data")
            runOnUiThread {
                updateCurrent(parseCurrent(current,daily.optJSONObject(0)))
                updateHourly(parseHourly(hourly))
                updateDaily(parseDaily(daily))
            }
        }
    }
    //update current
    private fun updateCurrent(forecast:Forecast){
        temp_id.text = tempC(""+forecast.temp)
        imgCondition(forecast.conditions, current_img_id)
        high_low_id.text = tempC(""+forecast.high) + " / " + tempC(""+forecast.low)
        conditions_id.text = forecast.conditions.toUpperCase()
        humidity_id.text = ""+forecast.humidity+"%"
    }
    //parse current object
    private fun parseCurrent(result: JSONObject, daily:JSONObject): Forecast {
        return Forecast(result.optDouble("temperature"),daily.optDouble("temperatureHigh"),daily.optDouble("temperatureLow")
            ,result.optDouble("humidity")*100.toInt(), result.optString("icon"))
    }
    //update hourly
    private fun updateHourly(forecasts:Array<Forecast>){

    }
    //parse hourly array
    private fun parseHourly(result: JSONArray): Array<Forecast> {
        var size = 0
        var forecasts = Array<Forecast>(size){Forecast(0.0,0.0,0.0,0.0,"sunny")}
        return forecasts
    }
    //update daily
    private fun updateDaily(forecasts:Array<Forecast>){

    }
    //parse daily array
    private fun parseDaily(result: JSONArray): Array<Forecast> {
        var size = 0
        var forecasts = Array<Forecast>(size){Forecast(0.0,0.0,0.0,0.0,"sunny")}
        return forecasts
    }

    //get image based off of conditions
    private fun imgCondition(condition:String, id:ImageView) {
        when(condition.toLowerCase()){
            "clear-day","clear-night", "wind" -> id.setImageResource(R.drawable.sunny)
            "rain", "sleet" -> id.setImageResource(R.drawable.rainy)
            "snow" -> id.setImageResource(R.drawable.snowy)
            "cloudy","partly-cloudy-day", "partly-cloudy-night" -> id.setImageResource(R.drawable.cloudy)
        }
    }

    //display the corresponding temperature with degree symbol and celsius sign
    private fun tempC(temp:String):String{
        return (temp + 0x00B0.toChar() + "C")
    }

    //round one digit
    private fun tForm(n:Float):Float{
        return round(n*100)/100
    }

    //use this class to store forecasts
    data class Forecast(val temp: Double, val high: Double, val low: Double, val humidity: Double, val conditions:String)
}
