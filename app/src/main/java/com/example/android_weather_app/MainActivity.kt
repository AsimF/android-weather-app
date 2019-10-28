package com.example.android_weather_app

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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
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
            runOnUiThread { //must use this, can't run UI on background thread
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
        return Forecast(result.optDouble("temperature").round(1),daily.optDouble("temperatureHigh").round(1),daily.optDouble("temperatureLow").round(1)
            ,(result.optDouble("humidity")*100).round(1), result.optString("icon"))
    }
    //update hourly (yes, I could use a loop here...)
    private fun updateHourly(forecasts:ArrayList<Forecast>){
        hr1_temp_id.text = tempC(""+forecasts[0].temp)
        imgCondition(forecasts[0].conditions, hr1_img_id)
        hr1_humid_id.text = ""+forecasts[0].humidity+"%"
        hr2_temp_id.text = tempC(""+forecasts[1].temp)
        imgCondition(forecasts[1].conditions, hr2_img_id)
        hr2_humid_id.text = ""+forecasts[1].humidity+"%"
        hr3_temp_id.text = tempC(""+forecasts[2].temp)
        imgCondition(forecasts[2].conditions, hr3_img_id)
        hr3_humid_id.text = ""+forecasts[2].humidity+"%"
        hr4_temp_id.text = tempC(""+forecasts[3].temp)
        imgCondition(forecasts[3].conditions, hr4_img_id)
        hr4_humid_id.text = ""+forecasts[3].humidity+"%"
        hr5_temp_id.text = tempC(""+forecasts[4].temp)
        imgCondition(forecasts[4].conditions, hr5_img_id)
        hr5_humid_id.text = ""+forecasts[4].humidity+"%"
    }
    //parse hourly array
    private fun parseHourly(result: JSONArray): ArrayList<Forecast> {
        var forecasts = ArrayList<Forecast>(5)
        updateTimes()
        for (i in 1..16 step 3){
            var r = result.optJSONObject(i)
            forecasts.add(Forecast(r.optDouble("temperature").round(1),r.optDouble("temperatureHigh").round(1),r.optDouble("temperatureLow").round(1)
                ,(r.optDouble("humidity")*100).round(1), r.optString("icon")))
        }
        return forecasts
    }
    //update daily
    private fun updateDaily(forecasts:ArrayList<Forecast>){
        day1_hl_id.text =  tempC(""+forecasts[0].high.toInt()) + " / " + tempC(""+forecasts[0].low.toInt())
        day1_humid_id.text = ""+forecasts[0].humidity+"%"
        imgCondition(forecasts[0].conditions, day1_img_id)
        day2_hl_id.text =  tempC(""+forecasts[1].high.toInt()) + " / " + tempC(""+forecasts[1].low.toInt())
        day2_humid_id.text = ""+forecasts[1].humidity+"%"
        imgCondition(forecasts[1].conditions, day2_img_id)
        day3_hl_id.text =  tempC(""+forecasts[2].high.toInt()) + " / " + tempC(""+forecasts[2].low.toInt())
        day3_humid_id.text = ""+forecasts[2].humidity+"%"
        imgCondition(forecasts[2].conditions, day3_img_id)
    }
    //parse daily array
    private fun parseDaily(result: JSONArray): ArrayList<Forecast> {
        var forecasts = ArrayList<Forecast>(3)
        updateDays()
        for (i in 1..3){
            var r = result.optJSONObject(i)
            forecasts.add(Forecast(r.optDouble("temperature").round(1),r.optDouble("temperatureHigh").round(1),r.optDouble("temperatureLow").round(1)
                ,(r.optDouble("humidity")*100).round(1), r.optString("icon")))
        }
        return forecasts
    }

    //update the times
    private fun updateTimes(){
        val sdf = SimpleDateFormat("HH")
        hr1_name_id.text = sdf.format(getHrsFrom(1)) + "h"
        hr2_name_id.text = sdf.format(getHrsFrom(4)) + "h"
        hr3_name_id.text = sdf.format(getHrsFrom(7)) + "h"
        hr4_name_id.text = sdf.format(getHrsFrom(10)) + "h"
        hr5_name_id.text = sdf.format(getHrsFrom(13)) + "h"
    }
    //update the days
    private fun updateDays(){
        val sdf = SimpleDateFormat("EEEE")
        day1_name_id.text = sdf.format(getDaysFrom(1))
        day2_name_id.text = sdf.format(getDaysFrom(2))
        day3_name_id.text = sdf.format(getDaysFrom(3))
    }
//    //get the max of 3hrs per day ***EDIT: Not needed, already calculates the max/min w/DarkSky
//    private fun maxDaily(){
//
//    }

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

    //use this class to store forecasts
    data class Forecast(val temp: Double, val high: Double, val low: Double, val humidity: Double, val conditions:String)
    //round to certain digit
    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
    //calculate the date certain days from current
    fun getDaysFrom(daysFrom: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, +daysFrom)

        return calendar.time
    }
    //calculate the date certain hrs from current
    fun getHrsFrom(hrsFrom: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, +hrsFrom)

        return calendar.time
    }
}
