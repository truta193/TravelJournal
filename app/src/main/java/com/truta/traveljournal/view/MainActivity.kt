package com.truta.traveljournal.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.truta.traveljournal.R
import com.truta.traveljournal.TravelJournalApplication
import com.truta.traveljournal.databinding.ActivityMainBinding
import com.truta.traveljournal.viewmodel.HomeViewModel
import com.truta.traveljournal.viewmodel.HomeMemoryModelFactory

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by viewModels {
        HomeMemoryModelFactory((application as TravelJournalApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayout
        val navigationView = binding.navigationView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentHost) as NavHostFragment
        navController = navHostFragment.navController


        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        NavigationUI.setupWithNavController(navigationView, navController)
        navigationView.setNavigationItemSelectedListener(this)

        onBackPressedDispatcher.addCallback(this) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        if (savedInstanceState == null) {
            navController.navigate(R.id.homeFragment)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.homeFragment -> navController.navigate(R.id.homeFragment)
            R.id.testFragment -> navController.navigate(R.id.testFragment)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}
