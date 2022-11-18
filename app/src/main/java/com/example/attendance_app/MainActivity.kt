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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.attendance_app.Adaptar.HomeClassAdaptar
import com.google.android.gms.location.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.type.DateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity()  {
    private lateinit var currLocation:String;
    private lateinit var locationCallback: LocationCallback
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LayoutManager
    lateinit var recyclerAdapatar:HomeClassAdaptar
    lateinit var classesList:ArrayList<SemesterClasses>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermission()
        recyclerView = findViewById(R.id.recycleViewHome)
        layoutManager = LinearLayoutManager(this)
        classesList = arrayListOf()
        recyclerView.layoutManager = layoutManager

        addClassData()
        getAllClasses()
       // sendStudentAttendance();
       // getCurrentLocation()
    }

    private  fun getAllClasses(){
        val ref = FirebaseDatabase.getInstance().getReference("4 CSE")

        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(classSnapShot in snapshot.children){
                        val user = classSnapShot.getValue(SemesterClasses::class.java)
                        if (user != null) {
                            classesList.add(user)
                        }
                    }
                    recyclerView.adapter = HomeClassAdaptar(applicationContext,classesList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private  fun sendStudentAttendance(){
    val ref = FirebaseDatabase.getInstance().getReference("4 CSE Portal")
        val refTeacherData = FirebaseDatabase.getInstance().getReference("4 CSE")
    val refOfAtte = FirebaseDatabase.getInstance().getReference("4 CSE ATTENDANCE")
        val student = StudentAttendance()
        student.name = "Priyanshu m"
    ref.get().addOnSuccessListener {
        if(it.exists()){
            val name = it.children.forEach {
                val subject = it.key;
                val portalOpen = it.value
                if(portalOpen == true){
                    refTeacherData.child(subject.toString()).get().addOnSuccessListener {
                        val location = it.child("location").value
                        val startPoint = location.toString().split(":")
                        val locate = Location("Location");
                        locate.latitude = startPoint[0].toDouble();
                        locate.longitude = startPoint[1].toDouble();
                        if(distanceCalculate(locate,locate)<=50){
                            refOfAtte.child(subject.toString()).child("29 Dec 20222").child(student.name).setValue(student)
                        }
                    }

                }
            }
        }
    }
    }

    private  fun sendTeacherData(){
        val teacherData:TeacherData = TeacherData()
        teacherData.isOpen  = true;
        teacherData.collegeYear = 4;
        teacherData.stream = "CSE";
        teacherData.subject = "QSA";
        teacherData.time = getCurrentTime();
        teacherData.location = currLocation;
        val ref = FirebaseDatabase.getInstance().getReference(teacherData.collegeYear.toString()+" "+teacherData.stream)
        val refOfPortal = FirebaseDatabase.getInstance().getReference(teacherData.collegeYear.toString()+" "+teacherData.stream +" Portal")
        val classId = ref.push().key
        refOfPortal.child(teacherData.subject).setValue(true)
        ref.child(teacherData.subject).setValue(teacherData).addOnCompleteListener {
           // Toast.makeText(this,"ADDED SUCCESSFULLY",Toast.LENGTH_LONG).show()
        }

    }

    private  fun getCurrentTime():String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        return formatted;
    }

    private fun addClassData(){
        val classDetails = AddClassButton();
        classDetails.collegeYear = 4;
        classDetails.subject = "Compiler Design"
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

    private fun distanceCalculate(locationA: Location,locationB: Location):Int{
        val startPoint = Location("Start")
        startPoint.latitude = locationA.latitude
        startPoint.longitude = locationA.longitude
        val endPoint = Location("Start")
        endPoint.latitude = locationB.latitude
        endPoint.longitude = locationB.longitude
        return startPoint.distanceTo(endPoint).toInt()
    }

}

//locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,50).setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(30).setMaxUpdateDelayMillis(100).build()