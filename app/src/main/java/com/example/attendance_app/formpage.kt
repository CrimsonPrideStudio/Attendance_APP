package com.example.attendance_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle

class formpage : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    var semesters = arrayOf<String?>("C", "Data structures",
        "Interview prep", "Algorithms",
        "DSA with java", "OS")

    val years= arrayOf<String?>("2019","2020","2021","2022")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form)


        val yearSpinner = findViewById<Spinner>(R.id.yearSpinner)
        yearSpinner.onItemSelectedListener = this

        val yearArrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            years)

        yearArrayAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)

        yearSpinner.adapter = yearArrayAdapter

        val streamSpinner = findViewById<Spinner>(R.id.streamSpinner)
        streamSpinner.onItemSelectedListener = this

        val streamArrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            semesters)

        streamArrayAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)

        streamSpinner.adapter = streamArrayAdapter
    }

    override fun onItemSelected(parent: AdapterView<*>?,
                                view: View, position: Int,
                                id: Long) {
        Toast.makeText(applicationContext,
            semesters[position],
            Toast.LENGTH_LONG)
            .show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}