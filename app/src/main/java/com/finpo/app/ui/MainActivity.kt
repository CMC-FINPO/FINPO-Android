package com.finpo.app.ui

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.finpo.app.NavGraphDirections
import com.finpo.app.R
import com.finpo.app.databinding.ActivityMainBinding
import com.finpo.app.ui.common.BaseActivity
import com.finpo.app.utils.dp
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController
    var isMovedHomeBySelectedItem = false
    private var destinationId = R.id.homeFragment
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
            destinationId = destination.id

            if(destination.id in bottomItemIds) navBarVisible()
            else navBarInvisible()

            window.statusBarColor = if(destination.id == R.id.homeFragment || destination.id == R.id.policyDetailFragment || destination.id == R.id.participationListFragment) ContextCompat.getColor(this, R.color.gray_g09)
            else ContextCompat.getColor(this, R.color.white_w01)
        }

        binding.navBar.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if(destinationId !in bottomItemIds) return@addOnLayoutChangeListener

            if(bottom < oldBottom) navBarInvisible()
            else navBarVisible()
        }

        processIntent(intent)
    }

    private fun navBarVisible() {
        binding.navBar.visibility = View.VISIBLE
        val layoutParams = binding.navHost.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.bottomMargin = 60.dp
        binding.navHost.layoutParams = layoutParams
    }

    private fun navBarInvisible() {
        binding.navBar.visibility = View.INVISIBLE
        val layoutParams = binding.navHost.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.bottomMargin = 0.dp
        binding.navHost.layoutParams = layoutParams
    }

    private fun processIntent(intent: Intent?) {
        val startId = intent?.getIntExtra("startId", R.id.homeFragment) ?: R.id.homeFragment
        val bulletinId = intent?.getIntExtra("bulletinId", -1) ?: -1

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.homeFragment, true)
            .build()
        when (startId) {
            R.id.policyDetailFragment -> navController.navigate(
                NavGraphDirections.actionGlobalPolicyDetailFragment(
                    bulletinId
                )
            )
            else -> navController.navigate(startId, null, navOptions)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        processIntent(intent)
    }

    override fun onBackPressed() {
        if(navController.currentDestination?.id in bottomItemIds) doDelayFinish()
        else super.onBackPressed()
    }
}