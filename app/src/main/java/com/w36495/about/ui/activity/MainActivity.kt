package com.w36495.about.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.w36495.about.R
import com.w36495.about.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSplashScreen()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
    }

    private fun initSplashScreen() {
        splashScreen = installSplashScreen()
    }

    private fun setupBottomNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHost.navController

        binding.mainBottomNavigation.apply {
            setupWithNavController(navController)

            setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.nav_topicList_fragment -> {
                        navController.navigate(R.id.nav_topicList_fragment)
                        true
                    }
                    R.id.nav_setting_fragment -> {
                        navController.navigate(R.id.nav_setting_fragment)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}