package fr97.movieinfo.feature.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr97.movieinfo.R

import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    private lateinit var navController: NavController

    private lateinit var navigationView: NavigationView

    private lateinit var drawer: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val navController = Navigation.findNavController(this, R.id.mainNavFragment)
//        navController = Navigation.findNavController(this, R.id.mainNavFragment)
        bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        // Main Navigation Setup
//        drawer = findViewById(R.id.drawer_layout)
//        navigationView = findViewById(R.id.nav_view)
        navController = Navigation.findNavController(this, R.id.mainNavFragment)

//        NavigationUI.setupActionBarWithNavController(this, navController, drawer)
        NavigationUI.setupActionBarWithNavController(this, navController)
        NavigationUI.setupWithNavController(bottomNavigation, navController);

//        navigationView.setNavigationItemSelectedListener(this);
//        NavigationUI.setupWithNavController(bottomNavigation, navController)

    }

    override fun onBackPressed() {
        super.onBackPressed()
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }


}
