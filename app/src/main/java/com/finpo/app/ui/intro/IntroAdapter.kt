package com.finpo.app.ui.intro

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.finpo.app.ui.intro.default_info.DefaultInfoFragment
import com.finpo.app.ui.intro.login.LoginFragment
import com.finpo.app.ui.intro.terms_conditions.TermsConditionsFragment

class IntroAdapter(activity: IntroActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3 //TODO constants 사용

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> TermsConditionsFragment()
            2 -> DefaultInfoFragment()
            else -> throw IllegalArgumentException("Cannot create fragment at page: $position")
        }
    }
}