package com.finpo.app.ui.intro

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class IntroAdapter(activity: IntroActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            else -> throw IllegalArgumentException("Cannot create fragment at page: $position")
        }
    }
}