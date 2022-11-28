package com.example.attendance_app.Adaptar

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_app.R
import com.example.attendance_app.DataClass.SemesterClasses

class HomeClassAdaptar(val context: Context,val classList:ArrayList<SemesterClasses>):RecyclerView.Adapter<HomeClassAdaptar.ClassViewHolder>() {
    private  lateinit var  mListener:onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }
    class ClassViewHolder(view: View,listener: onItemClickListener):RecyclerView.ViewHolder(view){
     val subjectAndSemText: TextView = view.findViewById(R.id.subjectAndSemText)
        val teacherNameText:TextView = view.findViewById(R.id.teacherName)
        val lastModifiedText:TextView = view.findViewById(R.id.lastUpdated)
        val totalStudents:TextView = view.findViewById(R.id.totalStudents)
        val presentStudents:TextView = view.findViewById(R.id.presentStudent)
        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.home_card_view,parent,false)
        return ClassViewHolder(view,mListener)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val value = classList[position];
        holder.subjectAndSemText.text = value.subject.toString()+","+value.collegeYear.toString();
        holder.teacherNameText.text = value.teacherName;
        holder.lastModifiedText.text = "20,November - 2022"
        holder.totalStudents.text = value.totalStudent;
        holder.presentStudents.text = "0";
    }

    override fun getItemCount(): Int {
        return classList.size;
    }
}