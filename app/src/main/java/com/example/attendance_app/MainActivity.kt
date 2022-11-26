package com.example.attendance_app

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.attendance_app.Adaptar.HomeClassAdaptar
import com.example.attendance_app.DataClass.AddClassButton
import com.example.attendance_app.DataClass.SemesterClasses
import com.example.attendance_app.DataClass.StudentAttendance
import com.example.attendance_app.DataClass.TeacherData
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    private lateinit var currLocation: String;
    private lateinit var locationCallback: LocationCallback
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LayoutManager
    lateinit var recyclerAdapatar: HomeClassAdaptar
    lateinit var classesList: ArrayList<SemesterClasses>

    lateinit var navBottom: NavigationView


    lateinit var streanHome: Spinner
    lateinit var yearHome: Spinner

    lateinit var addClassFlaotingBtn: FloatingActionButton
    lateinit var dialog: AlertDialog;
    lateinit var dialogBuilder: AlertDialog.Builder;

    //FORM FLOATING ACTION BUTTON
    lateinit var streamSpinners: Spinner
    lateinit var yearSpinners: Spinner
    lateinit var subjectNameForm:EditText
    lateinit var teacherNameForm:EditText
    lateinit var totalStudentForm:EditText
    lateinit var createButtonForm:Button
    lateinit var cancelButtonForm:Button
    @SuppressLint("InflateParams")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // sendStudentAttendance();
        // getCurrentLocation()
    }


}

//locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,50).setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(30).setMaxUpdateDelayMillis(100).build()