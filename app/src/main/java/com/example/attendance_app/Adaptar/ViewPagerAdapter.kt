package com.example.attendance_app.Adaptar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.attendance_app.Fragment.Dashboard
import com.example.attendance_app.Fragment.Profile
import com.example.attendance_app.Fragment.Setting


class ViewPagerAdapter(activity: FragmentActivity, private val fragments: List<Fragment>) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]


}
