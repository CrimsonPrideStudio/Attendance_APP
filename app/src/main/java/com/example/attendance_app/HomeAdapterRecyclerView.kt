package com.example.attendance_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class HomeAdapterRecyclerView:RecyclerView.Adapter<HomeAdapterRecyclerView.ViewHolder>() {

    private val subjects= arrayOf("Hindi","English","Maths","Hindi","English","Maths","Hindi","English","Maths")
    private val teacherName= arrayOf("Hina","Enna","Minna","Hina","Enna","Minna","Hina","Enna","Minna")
    private val lastUpdate= arrayOf("1,Nov 2022","1,Nov 2022","1,Nov 2022","1,Nov 2022","1,Nov 2022","1,Nov 2022","1,Nov 2022","1,Nov 2022","1,Nov 2022")
    private val present= arrayOf("50","57","10","50","57","10","50","57","10")
    private val total= arrayOf("55","61","10","55","61","10","55","61","10")

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var SubjectSem : TextView
        var TeacherName:TextView
        var LastUpdate:TextView
        var Present:TextView
        var Total:TextView

        init {
            SubjectSem = itemView.findViewById(R.id.subjectSem)
            TeacherName = itemView.findViewById(R.id.teacherName)
            LastUpdate = itemView.findViewById(R.id.Date)
            Present = itemView.findViewById(R.id.present)
            Total = itemView.findViewById(R.id.total)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.home_card_view,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.SubjectSem.text=subjects[position]
        holder.TeacherName.text=teacherName[position]
        holder.LastUpdate.text=lastUpdate[position]
        holder.Present.text=present[position]
        holder.Total.text=total[position]

        holder.itemView.setOnClickListener{v:View ->
            Toast.makeText(v.context,"card clicked",Toast.LENGTH_SHORT).show()
        }
    }


    override fun getItemCount(): Int {
        return subjects.size
    }


}