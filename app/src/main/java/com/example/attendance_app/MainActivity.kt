package com.example.attendance_app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.firebase.database.FirebaseDatabase
import com.google.type.DateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity()  {
    private lateinit var currLocation:String;
    private lateinit var locationCallback: LocationCallback
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermission()
        //addClassData()
        getCurrentLocation()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private  fun sendTeacherData(){

        val teacherData:TeacherData = TeacherData()
        teacherData.collegeYear = 4;
        teacherData.stream = "CSE";
        teacherData.subject = "python";
        teacherData.time = getCurrentTime();
        teacherData.location = currLocation;
        val ref = FirebaseDatabase.getInstance().getReference(teacherData.collegeYear.toString()+" "+teacherData.stream)
        val classId = ref.push().key
        ref.child(teacherData.subject).setValue(teacherData).addOnCompleteListener {
           // Toast.makeText(this,"ADDED SUCCESSFULLY",Toast.LENGTH_LONG).show()
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private  fun getCurrentTime():String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        return formatted;
    }

    private fun addClassData(){
        val classDetails = AddClassButton();
        classDetails.collegeYear = 4;
        classDetails.subject = "python"
        classDetails.teacherName="Khusboo MAm"
        classDetails.stream = "CSE"
        classDetails.totalStudent = "166";

        val ref = FirebaseDatabase.getInstance().getReference(classDetails.collegeYear.toString()+" "+classDetails.stream)
        val classId = ref.push().key
        classDetails.classID = classId.toString();
        ref.child(classDetails.subject).setValue(classDetails).addOnCompleteListener {
            Toast.makeText(this,"ADDED SUCCESSFULLY",Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission():Boolean{
        if ((ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
            && (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            return true;
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),100)
            return true;
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
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
                    if (it != null) {
                        currLocation = it.result.latitude.toString() +":"+it.result.longitude.toString()
                        sendTeacherData()
                        //Toast.makeText(this@MainActivity, contactDbList[0].personNumber.toString()+""+location.latitude.toString() + "  " + location.longitude.toString() , Toast.LENGTH_SHORT).show()
                    } else {
                        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,50).setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(30).setMaxUpdateDelayMillis(100).build()
                        fusedLocationClient.requestLocationUpdates(
                            locationRequest, locationCallback,
                            Looper.myLooper()
                        )
                    }

                }
            }else{

                //startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }

        }
    }




}

//locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,50).setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(30).setMaxUpdateDelayMillis(100).build()