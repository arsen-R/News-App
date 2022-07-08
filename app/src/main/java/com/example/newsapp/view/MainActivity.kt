package com.example.newsapp.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.NewsApplication
import com.example.newsapp.NewsNavGraphDirections
import com.example.newsapp.R
import com.example.newsapp.api.ArticleNewsApi
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val bottomNavigationView: BottomNavigationView by lazy { findViewById(R.id.bottomNavigationView) }
    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
    }
    private val navController: NavController by lazy {
        navHostFragment.navController
    }
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.recentNewsFragment,
            R.id.headlinesNewsFragment,
            R.id.savedNewsFragment,
            R.id.settingsFragment
        ).build()

        toolbar.setupWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.articleNewsFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                R.id.searchFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.searchNews -> {
                Toast.makeText(this@MainActivity, "Search", Toast.LENGTH_SHORT).show()
                val action: NavDirections = NewsNavGraphDirections.actionGlobalSearchFragment()
                navController.navigate(action)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}