package com.finpo.app.ui

import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.finpo.app.R
import com.finpo.app.databinding.ActivityMainBinding
import com.finpo.app.ui.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private var destinationId = R.id.homeFragment

    override fun init() {
        binding.navBar.itemIconTintList = null
        val navigationFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navigationFragment.navController
        NavigationUI.setupWithNavController(binding.navBar, navController)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        binding.navBar.setOnItemReselectedListener {  }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.homeFragment || destination.id == R.id.communityFragment || destination.id == R.id.alarmFragment || destination.id == R.id.myPageFragment) {
                binding.navBar.visibility = View.VISIBLE
                destinationId = destination.id
            }
            else {
                binding.navBar.visibility = View.GONE
            }

            window.statusBarColor = if(destination.id == R.id.homeFragment) ContextCompat.getColor(this, R.color.gray_g09)
            else ContextCompat.getColor(this, R.color.white_w01)
        }
    }

    override fun onBackPressed() {
        if(destinationId == R.id.homeFragment)  doDelayFinish()
        else super.onBackPressed()
    }
}