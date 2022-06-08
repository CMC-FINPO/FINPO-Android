package com.finpo.app.ui.intro

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.finpo.app.R

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

@BindingAdapter("introProgressBar")
fun setProgressBar(progressbar: ProgressBar, currentPage: Int) {
    progressbar.progress = when(currentPage) {
        0, 1, 2, 3, 4 -> currentPage
        else -> currentPage - 1
    }
}

@BindingAdapter("introProgressText")
fun setIntroProgressText(textView: TextView, currentPage: Int) {
    textView.text = String.format(textView.context.getString(R.string.intro_progress_text), when(currentPage) {
        0, 1, 2, 3, 4 -> currentPage
        else -> currentPage - 1
    })
}

@BindingAdapter("introButtonText")
fun setIntroButtonText(textView: TextView, currentPage: Int) {
    textView.text = when(currentPage) {
        1 -> "동의하기"
        2 -> "다음"
        3, 4, 6, 7 -> "선택 완료"
        5 -> "나중에 할게요"
        else -> "시작하기"
    }
}