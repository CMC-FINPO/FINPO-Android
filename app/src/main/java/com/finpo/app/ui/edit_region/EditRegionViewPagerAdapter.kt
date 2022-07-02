package com.finpo.app.ui.edit_region

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.finpo.app.ui.alarm.AlarmFragment
import com.finpo.app.ui.edit_region.edit_region_interest.EditRegionInterestFragment
import com.finpo.app.ui.edit_region.edit_region_living.EditRegionLivingFragment

class EditRegionViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> EditRegionInterestFragment()
            else -> EditRegionLivingFragment()
        }
    }
}