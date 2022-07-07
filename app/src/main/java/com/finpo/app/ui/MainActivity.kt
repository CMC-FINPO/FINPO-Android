package com.finpo.app.ui

import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.finpo.app.R
import com.finpo.app.databinding.ActivityMainBinding
import com.finpo.app.ui.common.BaseActivity
import com.finpo.app.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController
    var isMovedHomeBySelectedItem = false
    private val bottomItemIds = listOf(R.id.homeFragment, R.id.communityFragment, R.id.bookmarkFragment, R.id.myPageFragment)

    override fun init() {
        binding.navBar.itemIconTintList = null
        val navigationFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navigationFragment.navController
        NavigationUI.setupWithNavController(binding.navBar, navController)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        binding.navBar.setOnItemReselectedListener {  }

        binding.navBar.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navController)
            if(item.itemId == R.id.homeFragment) isMovedHomeBySelectedItem = true
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id != R.id.homeFragment) isMovedHomeBySelectedItem = false

            binding.navBar.visibility = if(destination.id in bottomItemIds)
                 View.VISIBLE
            else View.GONE

            window.statusBarColor = if(destination.id == R.id.homeFragment || destination.id == R.id.policyDetailFragment || destination.id == R.id.participationListFragment) ContextCompat.getColor(this, R.color.gray_g09)
            else ContextCompat.getColor(this, R.color.white_w01)
        }

        val startId = intent.getIntExtra("startId", R.id.homeFragment)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.homeFragment, true)
            .build()
        navController.navigate(startId, null, navOptions)
    }

    override fun onBackPressed() {
        if(navController.currentDestination?.id in bottomItemIds) doDelayFinish()
        else super.onBackPressed()
    }
}