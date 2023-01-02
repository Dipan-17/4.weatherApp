package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lat=intent.getStringExtra("lat")
        val longi=intent.getStringExtra("longi")

        getJsonData(lat,longi)
    }


    private fun getJsonData(lat:String?, longi:String?) {
        val queue = Volley.newRequestQueue(this)

        Toast.makeText(this, "Temp format: Current / Feels Like",Toast.LENGTH_LONG).show()

        val API_KEY: String = "70e6fd5562f6e12a4738469c5930ddf9"

        val url ="https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${longi}&appid=d782c3641dcc9cbcb8528dfb0245b3e4"

        //val url="https://api.openweathermap.org/data/2.5/weather?q=delhi&appid=d782c3641dcc9cbcb8528dfb0245b3e4"
        //Toast.makeText(this, url, Toast.LENGTH_LONG).show()

        // Request a string response from the provided URL.
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                // Toast.makeText(this,response.toString(),Toast.LENGTH_LONG).show()
                setValues(response)
            },
            {Toast.makeText(this, "ERROR",Toast.LENGTH_SHORT).show() })

        queue.add(jsonRequest)
    }

    private fun setValues(response: JSONObject){
        tvCity.text=response.getString("name");
        var ans1=response.getJSONObject("coord").getString("lat")
        var ans2=response.getJSONObject("coord").getString("lon")
        tvCoordinates.text="${ans1} , ${ans2}"

        tvWeather.text=response.getJSONArray("weather").getJSONObject(0)
           .getString("main")

       var tempr=response.getJSONObject("main").getString("temp")
        tempr=( (( (tempr).toFloat()  - 273.15) ).toInt()).toString()
        tvTemp.text="${tempr} °C "

        var feel=response.getJSONObject("main").getString("feels_like")
        feel=( (( (feel).toFloat()  - 273.15) ).toInt()).toString()

        tvTemp.text="${tempr} / ${feel} °C"



        var minTemp=response.getJSONObject("main").getString("temp_min")
        minTemp=( (( (minTemp).toFloat()  - 273.15) ).toInt()).toString()
        tvMinTemp.text="Min: ${ minTemp.toString() }"

        var maxTemp=response.getJSONObject("main").getString("temp_max")
        maxTemp=( (ceil( (maxTemp).toFloat()  - 273.15) ).toInt()).toString()
        tvMaxTemp.text="Max: ${ maxTemp.toString() }"

        tvPressure.text=response.getJSONObject("main").getString("pressure")
        tvHumidity.text=response.getJSONObject("main").getString("humidity")+"%"
        tvWindSpeed.text=response.getJSONObject("wind").getString("speed")+ " m/s"
        tvDegree.text="Degree: "+response.getJSONObject("wind").getString("deg")+"°"
        tvGust.text="Gust: "+response.getJSONObject("wind").getString("gust")+"°"

    }
}