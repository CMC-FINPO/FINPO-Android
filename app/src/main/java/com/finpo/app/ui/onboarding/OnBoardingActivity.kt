package com.finpo.app.ui.onboarding

import android.content.Intent
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.finpo.app.R
import com.finpo.app.databinding.ActivityOnboardingBinding
import com.finpo.app.di.FinpoApplication
import com.finpo.app.ui.common.BaseActivity
import com.finpo.app.ui.intro.IntroActivity
import com.finpo.app.ui.intro.IntroViewModel
import com.finpo.app.utils.PAGE
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private lateinit var viewPager: ViewPager2
    val viewModel by viewModels<OnBoardingViewModel>()

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewPager = binding.viewPager
        viewPager.adapter = OnBoardingAdapter(this)

        viewModel.goToIntroActivityEvent.observe(this) {
            FinpoApplication.prefs.setBoolean("showOnBoarding", false)
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }

        TabLayoutMediator(
            binding.tabLayout,
            viewPager
        ) { _, _ -> }.attach()
    }

    override fun onBackPressed() {
        when (viewPager.currentItem) {
            0 -> doDelayFinish()
            else -> viewModel.prevPage()
        }
    }
}