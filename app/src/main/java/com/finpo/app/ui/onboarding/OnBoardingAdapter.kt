package com.finpo.app.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.finpo.app.ui.onboarding.fragments.OnBoarding1Fragment
import com.finpo.app.ui.onboarding.fragments.OnBoarding2Fragment
import com.finpo.app.ui.onboarding.fragments.OnBoarding3Fragment
import com.finpo.app.ui.onboarding.fragments.OnBoarding4Fragment

class OnBoardingAdapter(activity: OnBoardingActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnBoarding1Fragment()
            1 -> OnBoarding2Fragment()
            2 -> OnBoarding3Fragment()
            3 -> OnBoarding4Fragment()
            else -> throw IllegalArgumentException("Cannot create fragment at page: $position")
        }
    }
}