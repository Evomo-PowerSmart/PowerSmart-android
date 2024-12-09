package com.evomo.powersmart.ui.screen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.evomo.powersmart.R
import com.evomo.powersmart.databinding.ActivityMainBinding
import com.evomo.powersmart.ui.utils.EdgeToEdgeUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.splashCondition }
        }
        EdgeToEdgeUtil.enableEdgeToEdge(this, binding.root)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navGraph = navController.navInflater.inflate(R.navigation.main_navigation)

        lifecycleScope.launch {
            viewModel.isUserLoggedIn.collectLatest { isLoggedIn ->
                navGraph.setStartDestination(if (isLoggedIn) R.id.homeFragment else R.id.loginFragment)
                if (navController.graph != navGraph) {
                    navController.graph = navGraph
                }
            }
        }

//        lifecycleScope.launch {
//            viewModel.themeValue.collect { theme ->
//                when (theme) {
//                    Theme.LIGHT -> {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                    }
//
//                    Theme.DARK -> {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                    }
//
//                    Theme.SYSTEM_DEFAULT -> {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//                    }
//                }
//            }
//        }
    }
}