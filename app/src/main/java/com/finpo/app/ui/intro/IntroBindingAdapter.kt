package com.finpo.app.ui.intro

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2

@BindingAdapter("currentPage")
fun setPage(
    viewPager2: ViewPager2,
    currentPage: Int
) {
    viewPager2.currentItem = currentPage
}

@BindingAdapter("introVisibility")
fun setVisibility(view: View, currentPage: Int) {
    view.visibility =
        if (currentPage == 0) View.GONE
        else View.VISIBLE
}