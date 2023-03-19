package com.example.attendance_app.Fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.attendance_app.Adaptar.ViewPagerAdapter
import com.example.attendance_app.R
import com.sagarkoli.chetanbottomnavigation.chetanBottomNavigation
import kotlinx.android.synthetic.main.activity_main.*

class Test : AppCompatActivity() {

    lateinit var navBar: chetanBottomNavigation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragments = listOf(Profile(), Dashboard(), Setting())

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(this, fragments)

        navBar=findViewById(R.id.navBar)

        navBar.add(chetanBottomNavigation.Model(1,R.drawable.profile))
        navBar.add(chetanBottomNavigation.Model(2,R.drawable.dashboard_icon))
        navBar.add(chetanBottomNavigation.Model(3,R.drawable.setting_icon))

        navBar.setCount(4,"10")

        navBar.setOnShowListener { item ->
            when (item?.id) {
                1 -> changeFragment(0)
                2 -> changeFragment(1)
                3 -> changeFragment(2)
            }
        }

        navBar.setOnClickMenuListener {

        }

        navBar.setOnReselectListener {

        }

        navBar.show(2,true)

    }

    private fun changeFragment(position: Int) {
        viewPager.currentItem = position
    }

}

