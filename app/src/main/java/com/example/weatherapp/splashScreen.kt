package com.example.weatherapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY

class splashScreen : AppCompatActivity() {
    //lateinit is used to give a guarantee that we will initialise the variable later
    lateinit var mfusedlocation:FusedLocationProviderClient
    private var myRequestCode=1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mfusedlocation=LocationServices.getFusedLocationProviderClient(this)

        //mfusedlocation.lastLocation


        getLastLocation()

//        Handler(Looper.getMainLooper()).postDelayed({
//            var intent= Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        },5000)

    }


    private fun getLastLocation() {
        if(checkPermission()){
            if( LocationEnable() ){
                //i.e GPS is enabled
                mfusedlocation.lastLocation.addOnCompleteListener{
                    task->
                    var location: Location?=task.result
                        if(location==null){
                            newLocation()
                        }else{

                            Handler(Looper.getMainLooper()).postDelayed({
                                     val intent=Intent(this, MainActivity::class.java)
                                    intent.putExtra("lat",location.latitude.toString())
                                    intent.putExtra("longi",location.longitude.toString())
                                    startActivity(intent)
                                    finish(); },3500)

                        }
                }
            }else{
                Toast.makeText(this, "Please turn on GPS ",Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }

    //if play services is restarted then access location once again
    private fun newLocation() {
        //private functions use nhi ho rhe hain
    }




    //we are checking if gps location is enabled
    private fun LocationEnable(): Boolean {
        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun RequestPermission() {
        ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION),myRequestCode)
    }

    //we are checking if permissions are enabled or not
    private fun checkPermission(): Boolean {
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED  ||
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ){
            return true
        }
        else{
            return false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == myRequestCode){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }

        }
    }
}