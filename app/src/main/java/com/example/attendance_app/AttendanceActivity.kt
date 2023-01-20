package com.example.attendance_app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_app.Adaptar.AttendanceAdapterView
import com.example.attendance_app.DataClass.StudentAttendance
import com.example.attendance_app.Fragment.Dashboard.Companion.currLocation
import com.example.attendance_app.Fragment.Dashboard.Companion.currentClass
import com.example.attendance_app.Fragment.Dashboard.Companion.currentStream
import com.example.attendance_app.Fragment.Dashboard.Companion.currentYear
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AttendanceActivity : AppCompatActivity() {
    lateinit var presentList:ArrayList<StudentAttendance>

    //region Recyclerview
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapatar: AttendanceAdapterView
    //endregion

    lateinit var pdfDownload:Button
    lateinit var startEndBtn:Button
    lateinit var classTitle:TextView
    lateinit var document:PdfDocument
     var startForResult: ActivityResultLauncher<Intent>? = null
    var currrentDate:String =""
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)
        classTitle = findViewById(R.id.classTitle)
        classTitle.text = intent.extras?.getString("ClassName")
        recyclerView = findViewById(R.id.attendaceRecyclerView)
        layoutManager = LinearLayoutManager(this)
        presentList = arrayListOf()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = AttendanceAdapterView(this,presentList)
        getPresetStudentList()

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val  intent = result.data

                    var uri: Uri? = null
                    if(result!=null){
                        uri = result.data?.data!!

                        if(document!=null){
                            var pfd: ParcelFileDescriptor? = null
                            try {
                                pfd= contentResolver.openFileDescriptor(uri,"w")
                                var fileOutputStream:FileOutputStream = FileOutputStream(pfd?.fileDescriptor)
                                document.writeTo(fileOutputStream)
                                document.close()
                                Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()
                            }catch (e:IOException){
                                Toast.makeText(this,"Error Saved",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }else{
                    Toast.makeText(this,result.resultCode.toString(),Toast.LENGTH_SHORT).show()
                }

        }
        pdfDownload = findViewById(R.id.pdfDownload)
        startEndBtn = findViewById(R.id.startEndBtn)
        pdfDownload.setOnClickListener {
            if(checkPermission()){
                genertaePDF()
            }
        }

        startEndBtn.setOnClickListener {
            if(startEndBtn.text == getString(R.string.start)){
                startEndBtn.text = "End"
                deletePreviousAttendance()
                sendTeacherData()
            }else{
                startEndBtn.text = getString(R.string.start)
                closePortal()
            }
        }
    }

    private  fun genertaePDF(){
        document = PdfDocument()
        var pageInfo:PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(1500,2080,1).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas
        var paintText = Paint()
        paintText.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD,Typeface.NORMAL))
        paintText.textSize = 50F
        paintText.setColor(ContextCompat.getColor(this,R.color.black))

        paintText.textAlign = Paint.Align.CENTER
        canvas.drawText("Computer Science And Engineering",
            750F,50F,paintText)

        paintText.textAlign = Paint.Align.LEFT
        paintText.textSize = 35F
        canvas.drawText("Subject: ${currentClass}",
            50F,150F,paintText)

        paintText.textAlign = Paint.Align.CENTER
        canvas.drawText("Year: 4th",
            900F,150F,paintText)

        paintText.textAlign = Paint.Align.RIGHT
        canvas.drawText("Date : 29 Nov 2023",
            1350F,150F,paintText)

        paintText.textSize = 100F
        canvas.drawLine(0F,200F,1500F,200F,paintText)

        paintText.textAlign = Paint.Align.LEFT
        paintText.textSize = 25F
        for (i in 0 until presentList.size){
          canvas.drawText("${i+1}): ${presentList[i].name}                       ${presentList[i].rollNumber}",
              50F,250F+i*50F,paintText)
        }
        document.finishPage(page)
        createFile()

    }

    private  fun createFile(){
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("application/pdf")
        intent.putExtra(Intent.EXTRA_TITLE,"20_nov_class_7.pdf")
        startForResult?.launch(intent)
    }

    private fun checkPermission(): Boolean {
        if ((ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)
            && (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            return true;
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                100
            )
            return true;
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getPresetStudentList(){
        val ref = FirebaseDatabase.getInstance().getReference("Attendance/$currentYear/$currentStream/$currentClass/")
            ref.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(student in snapshot.children){
                            val st = StudentAttendance()
                            st.name = student.value.toString().split("=")[1].replace("}","")

                            st.rollNumber = student.value.toString().split("=")[0].replace("{","")
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

    private  fun getCurrentDate(){
        val calendar = Calendar.getInstance()
        val dateFormat: SimpleDateFormat = SimpleDateFormat("DD MMM yyyy");
        val date = dateFormat.format(calendar.time)
        currrentDate = date.toString()
        //Toast.makeText(requireContext(),date.toString(),Toast.LENGTH_SHORT).show()
    }

    private fun sendTeacherData() {
        val ref = FirebaseDatabase.getInstance().getReference("Portal/${currentYear}/${currentStream}").child(
            currentClass
        ).setValue(currLocation)

    }
    private fun deletePreviousAttendance(){
        val ref = FirebaseDatabase.getInstance().getReference("Attendance/${currentYear}/${currentStream}").child(
            currentClass
        ).setValue(null)
    }

    private fun closePortal(){
        val ref = FirebaseDatabase.getInstance().getReference("Portal/${currentYear}/${currentStream}").child(
            currentClass
        ).setValue(null)
    }
    private  fun result(resultCode: Int,result: ActivityResult){
        if(result.resultCode== Activity.RESULT_OK){
            val  intent = result.data
            if(resultCode == 1 ){
                var uri: Uri? = null
                if(result!=null){
                    uri = result.data?.data!!

                    if(document!=null){
                        var pfd: ParcelFileDescriptor? = null
                        try {
                            pfd= contentResolver.openFileDescriptor(uri,"w")
                            var fileOutputStream:FileOutputStream = FileOutputStream(pfd?.fileDescriptor)
                            document.writeTo(fileOutputStream)
                            document.close()
                            Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()
                        }catch (e:IOException){
                            Toast.makeText(this,"Error Saved",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(this,"Error Saved",Toast.LENGTH_SHORT).show()
            }
        }
    }

}