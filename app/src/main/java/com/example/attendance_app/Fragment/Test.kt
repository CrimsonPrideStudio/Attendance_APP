package com.example.attendance_app.Fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.attendance_app.R
import com.sagarkoli.chetanbottomnavigation.chetanBottomNavigation

class Test : AppCompatActivity() {

    lateinit var navBar: chetanBottomNavigation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navBar=findViewById(R.id.navBar)

        navBar.add(chetanBottomNavigation.Model(1,R.drawable.profile))
        navBar.add(chetanBottomNavigation.Model(2,R.drawable.dashboard_icon))
        navBar.add(chetanBottomNavigation.Model(3,R.drawable.setting_icon))

        navBar.setCount(4,"10")

        navBar.setOnShowListener(object :chetanBottomNavigation.ShowListener {
            override fun onShowItem(item: chetanBottomNavigation.Model?) {
                Toast.makeText(applicationContext,"show",Toast.LENGTH_SHORT).show()
            }

        })

        navBar.setOnClickMenuListener(object :chetanBottomNavigation.ClickListener {
            override fun onClickItem(item: chetanBottomNavigation.Model?) {
                Toast.makeText(applicationContext,"onclick",Toast.LENGTH_SHORT).show()

            }

        })

        navBar.setOnReselectListener(object :chetanBottomNavigation.ReselectListener {
            override fun onReselectItem(item: chetanBottomNavigation.Model?) {
                Toast.makeText(applicationContext,"reselect",Toast.LENGTH_SHORT).show()

            }

        })

    navBar.show(2,true)

    }
}