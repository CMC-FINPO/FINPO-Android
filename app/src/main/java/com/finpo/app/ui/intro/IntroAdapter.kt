package com.finpo.app.ui.intro

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.finpo.app.ui.intro.additional_region.AdditionalRegionFragment
import com.finpo.app.ui.intro.default_info.DefaultInfoFragment
import com.finpo.app.ui.intro.finish.FinishFragment
import com.finpo.app.ui.intro.interest.InterestFragment
import com.finpo.app.ui.intro.living_area.LivingAreaFragment
import com.finpo.app.ui.intro.login.LoginFragment
import com.finpo.app.ui.intro.register_complete.RegisterCompleteFragment
import com.finpo.app.ui.intro.status_purpose.StatusPurposeFragment
import com.finpo.app.ui.intro.terms_conditions.TermsConditionsFragment
import com.finpo.app.utils.INTRO_PAGE_NUM

class IntroAdapter(activity: IntroActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = INTRO_PAGE_NUM

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> TermsConditionsFragment()
            2 -> DefaultInfoFragment()
            3 -> LivingAreaFragment()
            4 -> InterestFragment()
            5 -> RegisterCompleteFragment()
            6 -> StatusPurposeFragment()
            7 -> AdditionalRegionFragment()
            8 -> FinishFragment()
            else -> throw IllegalArgumentException("Cannot create fragment at page: $position")
        }
    }
}