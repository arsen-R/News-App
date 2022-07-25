package com.example.newsapp.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.NewsNavGraphDirections
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
    }
    private val navController: NavController by lazy {
        navHostFragment.navController
    }

    private lateinit var binding: ActivityMainBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.recentNewsFragment,
            R.id.headlinesNewsFragment,
            R.id.savedNewsFragment,
            R.id.settingsFragment
        ).build()

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.articleNewsFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.searchFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
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