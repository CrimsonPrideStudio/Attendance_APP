package com.example.attendance_app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback

class MainActivity : AppCompatActivity() {
    private lateinit var locationCallback: LocationCallback
    lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
    }
    private fun checkPermission(){
        if ((ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
            && (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
                    )
        ) {
            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.SEND_SMS),100)
        }
    }
    private fun getCurrentLocation() {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        ) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    val location: Location = it.result


                    if (location != null) {
                    } else {
                        val locationRequest: com.google.android.gms.location.LocationRequest =
                            com.google.android.gms.location.LocationRequest.create()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            locationRequest.priority = LocationRequest.QUALITY_HIGH_ACCURACY
                        }
                        locationRequest.interval = 10000
                        locationRequest.fastestInterval = 1000
                        locationRequest.numUpdates = 1
                        fusedLocationClient.requestLocationUpdates(
                            locationRequest, locationCallback,
                            Looper.myLooper()
                        )
                    }

                }
            } else {

                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }

        }
    }
}