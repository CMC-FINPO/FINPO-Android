package com.finpo.app.ui.intro

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MediatorLiveData
import androidx.viewpager2.widget.ViewPager2
import com.finpo.app.R
import com.finpo.app.model.local.CategoryId
import com.finpo.app.ui.intro.default_info.DefaultInfoLiveData
import com.finpo.app.utils.PAGE.ADDITIONAL_REGION
import com.finpo.app.utils.PAGE.AGREE
import com.finpo.app.utils.PAGE.DEFAULT_INFO
import com.finpo.app.utils.PAGE.FINISH
import com.finpo.app.utils.PAGE.INTEREST
import com.finpo.app.utils.PAGE.LOGIN
import com.finpo.app.utils.PAGE.REGION
import com.finpo.app.utils.PAGE.REGISTRATION
import com.finpo.app.utils.PAGE.STATE_PURPOSE

@BindingAdapter("currentPage")
fun setPage(
    viewPager2: ViewPager2,
    currentPage: Int
) {
    viewPager2.setCurrentItem(currentPage, false)
}

@BindingAdapter("introTopLayoutVisibility")
fun setIntroTopLayoutVisibility(view: View, currentPage: Int) {
    view.visibility =
        if (currentPage == LOGIN || currentPage == REGISTRATION) View.GONE
        else View.VISIBLE
}

@BindingAdapter("introButtonVisibility")
fun setVisibility(view: View, currentPage: Int) {
    view.visibility =
        if (currentPage == LOGIN) View.GONE
        else View.VISIBLE
}

@BindingAdapter("introSkipVisibility")
fun setIntroSkipVisibility(view: View, currentPage: Int) {
    view.visibility =
        if (currentPage > REGISTRATION && currentPage != FINISH) View.VISIBLE
        else View.GONE
}

@BindingAdapter("introProgressBar")
fun setProgressBar(progressbar: ProgressBar, currentPage: Int) {
    progressbar.progress = when (currentPage) {
        LOGIN, AGREE, DEFAULT_INFO, REGION, INTEREST -> currentPage
        else -> currentPage - 1
    }
    if(currentPage == FINISH)   progressbar.visibility = View.GONE
    else progressbar.visibility = View.VISIBLE
}

@BindingAdapter("introProgressText")
fun setIntroProgressText(textView: TextView, currentPage: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.intro_progress_text), when (currentPage) {
            LOGIN, AGREE, DEFAULT_INFO, REGION, INTEREST -> currentPage
            else -> currentPage - 1
        }
    )
    if(currentPage == FINISH)   textView.visibility = View.GONE
    else textView.visibility = View.VISIBLE
}

@BindingAdapter("introButtonText")
fun setIntroButtonText(textView: TextView, currentPage: Int) {
    textView.text = when (currentPage) {
        AGREE -> "동의하기"
        DEFAULT_INFO -> "다음"
        REGION, INTEREST, STATE_PURPOSE, ADDITIONAL_REGION -> "선택 완료"
        REGISTRATION -> "나중에 할게요"
        else -> "시작하기"
    }
}

@BindingAdapter("currentPage", "isTermsConditionsButtonEnabled",
    "isDefaultInfoButtonEnabled", "selectedDetailRegionText","userInterestData",
    "additionalRegionSelCount")
fun setAgreeButtonEnabled(
    button: Button,
    currentPage: Int,
    isTermsConditionsButtonEnabled: Boolean,
    isDefaultInfoButtonEnabled: Boolean,
    selectedDetailRegionText: String,
    userInterestData: MutableSet<CategoryId>,
    additionalRegionSelCount: Int
) {
    button.isEnabled = (currentPage == AGREE && isTermsConditionsButtonEnabled) ||
            (currentPage == DEFAULT_INFO && isDefaultInfoButtonEnabled) ||
            (currentPage == REGION && selectedDetailRegionText.isNotEmpty()) ||
            (currentPage == INTEREST && userInterestData.isNotEmpty()) ||
            (currentPage == REGISTRATION) ||
            (currentPage == STATE_PURPOSE) ||
            (currentPage == ADDITIONAL_REGION && (additionalRegionSelCount != 0)) ||
            (currentPage == FINISH)

    if(currentPage == REGISTRATION) {
        button.setBackgroundResource(R.drawable.bg_solid_g08_rounded_5)
        button.setTextColor(button.context.getColor(R.color.gray_g02))
    } else {
        button.setBackgroundResource(R.drawable.selector_main_button_bg)
        button.setTextColor(button.context.getColorStateList(R.color.selector_main_button_text))
    }
}