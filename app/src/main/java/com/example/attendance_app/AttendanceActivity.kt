package com.example.attendance_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_app.Adaptar.AttendanceAdapterView
import com.example.attendance_app.Adaptar.HomeClassAdaptar
import com.example.attendance_app.DataClass.StudentAttendance
import com.example.attendance_app.Fragment.Dashboard.Companion.currentClass
import com.example.attendance_app.Fragment.Dashboard.Companion.currentStream
import com.example.attendance_app.Fragment.Dashboard.Companion.currentYear
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AttendanceActivity : AppCompatActivity() {
    lateinit var presentList:ArrayList<StudentAttendance>

    //region Recyclerview
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapatar: AttendanceAdapterView
    //endregion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)
        recyclerView = findViewById(R.id.attendaceRecyclerView)
        layoutManager = LinearLayoutManager(this)
        presentList = arrayListOf()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = AttendanceAdapterView(this,presentList)
        getPresetStudentList()
    }
    private fun getPresetStudentList(){
        val ref = FirebaseDatabase.getInstance().getReference("Attendance/$currentYear/$currentStream/$currentClass/20 Nov 2022")
            ref.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(student in snapshot.children){
                            val st = StudentAttendance()
                            st.name = student.value.toString()
                            st.rollNumber = student.key.toString()
                            presentList.add(st)
                        }
                        val adaptar = AttendanceAdapterView(applicationContext, presentList)
                        recyclerView.adapter = adaptar
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}