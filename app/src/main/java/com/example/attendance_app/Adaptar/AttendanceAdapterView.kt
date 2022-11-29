package com.example.attendance_app.Adaptar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_app.DataClass.StudentAttendance
import com.example.attendance_app.R

class AttendanceAdapterView(val context: Context,val presentList:ArrayList<StudentAttendance>):RecyclerView.Adapter<AttendanceAdapterView.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val studentName :TextView = view.findViewById(R.id.studentName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attendance_cardview,parent,false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val value = presentList[position]
        holder.studentName.text = value.name;
    }

    override fun getItemCount(): Int {
       return presentList.size
    }
}