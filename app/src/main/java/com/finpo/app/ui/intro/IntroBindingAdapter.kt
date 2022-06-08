package com.finpo.app.ui.intro

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2

@BindingAdapter("currentPage")
fun setPage(
    viewPager2: ViewPager2,
    currentPage: Int
) {
    viewPager2.currentItem = currentPage
}