package com.finpo.app.ui.policy_detail

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import com.finpo.app.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


@BindingAdapter("safeNullText")
fun safeNullText(textView: TextView, safeNullText: String?) {
    textView.text = safeNullText ?: ""
}

@BindingAdapter("tabTextStyleBold")
fun TabLayout.tabTextStyle(tabTextStyleSize: String?) {
    val mTabLayout = this
    val (TabTextSelectedStyle, TabTextUnSelectedStyle) = if(tabTextStyleSize == "SMALL") listOf(R.style.TabTextSelected_14, R.style.TabTextUnSelected_14)
    else listOf(R.style.TabTextSelected, R.style.TabTextUnSelected)
    val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val tabLayout =
                (mTabLayout.getChildAt(0) as ViewGroup).getChildAt(tab!!.position) as LinearLayout
            val tabTextView = tabLayout.getChildAt(1) as TextView
            tabTextView.setTextAppearance(TabTextSelectedStyle)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            val tabLayout =
                (mTabLayout.getChildAt(0) as ViewGroup).getChildAt(tab!!.position) as LinearLayout
            val tabTextView = tabLayout.getChildAt(1) as TextView
            tabTextView.setTextAppearance(TabTextUnSelectedStyle)
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

    onTabSelectedListener.onTabSelected(mTabLayout.getTabAt(mTabLayout.selectedTabPosition))
    mTabLayout.addOnTabSelectedListener(onTabSelectedListener)
}

@BindingAdapter("policyStartDate", "policyEndDate", "policyPeriod")
fun setPeriod(textView: TextView, policyStartDate: String?, policyEndDate: String?, policyPeriod: String?) {
    textView.text = if(policyStartDate != null && policyEndDate != null) "$policyStartDate  -  $policyEndDate"
    else policyPeriod ?: ""
}