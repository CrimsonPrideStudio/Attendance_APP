package com.example.attendance_app.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_app.Adaptar.HomeClassAdaptar
import com.example.attendance_app.AttendanceActivity
import com.example.attendance_app.DataClass.AddClassButton
import com.example.attendance_app.DataClass.SemesterClasses
import com.example.attendance_app.DataClass.StudentAttendance
import com.example.attendance_app.DataClass.TeacherData
import com.example.attendance_app.R

import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Dashboard : Fragment(),AdapterView.OnItemSelectedListener {
    private lateinit var currLocation: String;
    private lateinit var locationCallback: LocationCallback
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
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
    lateinit var subjectNameForm: EditText
    lateinit var teacherNameForm: EditText
    lateinit var totalStudentForm: EditText
    lateinit var createButtonForm: Button
    lateinit var cancelButtonForm: Button
    lateinit var binding: View
    var i = 0;

    companion object {
        var currentYear = "4"
        var currentClass = "Python"
        var currentStream = "CSE"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (i==0) {

            i++
            binding = inflater.inflate(R.layout.home_page, container, false)
            val yearHome = binding.findViewById<Spinner>(R.id.yearHome)
            val streanHome = binding.findViewById<Spinner>(R.id.streamHome)

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            checkPermission()
            getCurrentLocation()

            //region RecyclerView
            recyclerView = binding.findViewById(R.id.recycleViewHome)
            layoutManager = LinearLayoutManager(activity)
            classesList = arrayListOf()
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = HomeClassAdaptar(requireContext(), arrayListOf())
            //endregion

            addClassFlaotingBtn = binding.findViewById(R.id.addClassFloatingButton)

            val contactPopupView: View = layoutInflater.inflate(R.layout.form, null)
            initializeForm(contactPopupView)

            //region Add Button Dialog
            addClassFlaotingBtn.setOnClickListener {
                dialog.show()
            }
            getAllClasses()
            createButtonForm.setOnClickListener {
                addClassData()
            }
            cancelButtonForm.setOnClickListener {
                dialog.hide()
            }
            //endregion

        }
        return binding
    }

    private fun initializeSpinners() {
        val streamAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.streams,
            android.R.layout.simple_spinner_item
        )
        val yearAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.years,
            android.R.layout.simple_spinner_item
        )
        streamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinners.adapter = yearAdapter
        yearSpinners.onItemSelectedListener = this
        streamSpinners.adapter = streamAdapter
        streamSpinners.onItemSelectedListener = this

    }

    private fun initializeForm(context:View){
        subjectNameForm = context.findViewById(R.id.subjectNameInput)
        teacherNameForm= context.findViewById(R.id.teacherNameInput)
        yearSpinners = context.findViewById(R.id.yearSpinner)
        streamSpinners = context.findViewById(R.id.streamSpinner)
        totalStudentForm = context.findViewById(R.id.totalStudentsInput)
        createButtonForm = context.findViewById(R.id.createBtn)
        cancelButtonForm = context.findViewById(R.id.cancelBtn)
        dialogBuilder = AlertDialog.Builder(requireContext(),R.style.customAlert)
        dialogBuilder.setView(context)
        dialog = dialogBuilder.create()
        initializeSpinners()
    }

    private fun getAllClasses() {
        val ref = FirebaseDatabase.getInstance().getReference("4 CSE")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (classSnapShot in snapshot.children) {
                        val user = classSnapShot.getValue(SemesterClasses::class.java)
                        if (user != null) {
                            classesList.add(user)
                        }
                    }
                    val adaptar = HomeClassAdaptar(binding.context, classesList)
                    recyclerView.adapter = adaptar
                    adaptar.setOnItemClickListener(object : HomeClassAdaptar.onItemClickListener{
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onItemClick(position: Int) {
                            val data = classesList[position]
                            currentYear = data.collegeYear.toString()
                            currentClass = data.subject.toString()
                            currentStream = data.stream.toString()
                            sendTeacherData()
                            val intent  = Intent(binding.context,AttendanceActivity::class.java)
                            startActivity(intent)
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun sendStudentAttendance() {
        val ref = FirebaseDatabase.getInstance().getReference("4 CSE Portal")
        val refTeacherData = FirebaseDatabase.getInstance().getReference("4 CSE")
        val refOfAtte = FirebaseDatabase.getInstance().getReference("4 CSE ATTENDANCE")
        val student = StudentAttendance()
        student.name = "Priyanshu m"
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                val name = it.children.forEach {
                    val subject = it.key;
                    val portalOpen = it.value
                    if (portalOpen == true) {
                        refTeacherData.child(subject.toString()).get().addOnSuccessListener {
                            val location = it.child("location").value
                            val startPoint = location.toString().split(":")
                            val locate = Location("Location");
                            locate.latitude = startPoint[0].toDouble();
                            locate.longitude = startPoint[1].toDouble();
                            if (distanceCalculate(locate, locate) <= 50) {
                                refOfAtte.child(subject.toString()).child("29 Dec 20222")
                                    .child(student.name).setValue(student)
                            }
                        }

                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendTeacherData() {
        val ref = FirebaseDatabase.getInstance().getReference("Portal/${currentYear}/${currentStream}").child(
            currentClass
        ).setValue(currLocation)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        return formatted;
    }

    private fun addClassData() {
        val classDetails = AddClassButton();
        classDetails.collegeYear = (yearSpinners.selectedItem.toString()[0].code) - '0'.code;
        classDetails.subject = subjectNameForm.text.toString()
        classDetails.teacherName = teacherNameForm.text.toString()
        classDetails.stream = streamSpinners.selectedItem.toString()
        classDetails.totalStudent = totalStudentForm.text.toString();
        val ref = FirebaseDatabase.getInstance()
            .getReference(classDetails.collegeYear.toString() + " " + classDetails.stream)
        val classId = ref.push().key
        classDetails.classID = classId.toString();
        ref.child(classDetails.subject).setValue(classDetails).addOnCompleteListener {
            classesList = arrayListOf()
            getAllClasses()
        }
    }

    private fun checkPermission(): Boolean {
        if ((ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
            && (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            return true;
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
            return true;
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentLocation() {
        val locationManager: LocationManager =
           context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        ) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    if (it != null) {
                        currLocation =
                            it.result.latitude.toString() + ":" + it.result.longitude.toString()

                        //Toast.makeText(this@MainActivity, contactDbList[0].personNumber.toString()+""+location.latitude.toString() + "  " + location.longitude.toString() , Toast.LENGTH_SHORT).show()
                    } else {
                        locationRequest =
                            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 50)
                                .setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(30)
                                .setMaxUpdateDelayMillis(100).build()
                        fusedLocationClient.requestLocationUpdates(
                            locationRequest, locationCallback,
                            Looper.myLooper()
                        )
                    }

                }
            } else {

                //startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }

        }
    }

    private fun distanceCalculate(locationA: Location, locationB: Location): Int {
        val startPoint = Location("Start")
        startPoint.latitude = locationA.latitude
        startPoint.longitude = locationA.longitude
        val endPoint = Location("Start")
        endPoint.latitude = locationB.latitude
        endPoint.longitude = locationB.longitude
        return startPoint.distanceTo(endPoint).toInt()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Toast.makeText(requireContext(), "AS", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}