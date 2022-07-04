package com.finpo.app.ui

import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.finpo.app.R
import com.finpo.app.databinding.ActivityMainBinding
import com.finpo.app.ui.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController

    override fun init() {
        binding.navBar.itemIconTintList = null
        val navigationFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navigationFragment.navController
        NavigationUI.setupWithNavController(binding.navBar, navController)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        binding.navBar.setOnItemReselectedListener {  }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.navBar.visibility = if(destination.id == R.id.homeFragment || destination.id == R.id.communityFragment || destination.id == R.id.bookmarkFragment || destination.id == R.id.myPageFragment)
                 View.VISIBLE
            else View.GONE

            window.statusBarColor = if(destination.id == R.id.homeFragment || destination.id == R.id.policyDetailFragment) ContextCompat.getColor(this, R.color.gray_g09)
            else ContextCompat.getColor(this, R.color.white_w01)
        }

        val startId = intent.getIntExtra("startId", R.id.homeFragment)
        navController.navigate(startId)
    }

    override fun onBackPressed() {
        if(navController.currentDestination?.id == R.id.homeFragment)  doDelayFinish()
        else super.onBackPressed()
    }
}