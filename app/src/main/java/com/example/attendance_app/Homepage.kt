package com.example.attendance_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Homepage : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        val drawerToggle: DrawerLayout =findViewById(R.id.nav_drawer)
        val navBar: NavigationView =findViewById(R.id.nav_view)

        toggle=ActionBarDrawerToggle(this,drawerToggle,R.string.navigation_drawer_open,R.string.navigation_drawer_close)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerToggle.addDrawerListener(toggle)
        toggle.syncState()

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}