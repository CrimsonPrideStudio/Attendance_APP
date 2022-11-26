package com.example.attendance_app.Fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
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

                when(item?.id){
                    1-> changeFragment(Profile())
                    2-> changeFragment(Dashboard())
                    3-> changeFragment(Setting())
                }

            }

        })

        navBar.setOnClickMenuListener(object :chetanBottomNavigation.ClickListener {
            override fun onClickItem(item: chetanBottomNavigation.Model?) {

            }

        })

        navBar.setOnReselectListener(object :chetanBottomNavigation.ReselectListener {
            override fun onReselectItem(item: chetanBottomNavigation.Model?) {

            }

        })

    navBar.show(2,true)

    }

    private fun changeFragment(fragment: Fragment){
        val fragManager=supportFragmentManager
        val fragTransaction=fragManager.beginTransaction()
        fragTransaction.replace(R.id.fragContainer,fragment)
        fragTransaction.commit()
    }
}

