package com.example.attendance_app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.attendance_app.Adaptar.ViewPagerAdapter
import com.example.attendance_app.R
import com.sagarkoli.chetanbottomnavigation.chetanBottomNavigation

class Test : AppCompatActivity() {

    lateinit var navBar: chetanBottomNavigation
    lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navBar = findViewById(R.id.navBar)
        viewPager = findViewById(R.id.viewPager)

        val fragments = listOf(Profile(), Dashboard(), Setting())
        viewPager.adapter = ViewPagerAdapter(this, fragments)

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

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                navBar.show(position + 1, true)
            }
        })

        viewPager.currentItem=1

        navBar.show(2,true)
    }

    private fun changeFragment(position: Int) {
        viewPager.currentItem = position
    }
}

