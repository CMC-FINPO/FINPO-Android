package com.finpo.app.ui.intro

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.finpo.app.R
import com.finpo.app.ui.intro.default_info.DefaultInfoLiveData
import com.finpo.app.utils.PAGE.ADDITIONAL_REGION
import com.finpo.app.utils.PAGE.AGREE
import com.finpo.app.utils.PAGE.DEFAULT_INFO
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

@BindingAdapter("introProgressBar")
fun setProgressBar(progressbar: ProgressBar, currentPage: Int) {
    progressbar.progress = when (currentPage) {
        LOGIN, AGREE, DEFAULT_INFO, REGION, INTEREST -> currentPage
        else -> currentPage - 1
    }
}

@BindingAdapter("introProgressText")
fun setIntroProgressText(textView: TextView, currentPage: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.intro_progress_text), when (currentPage) {
            LOGIN, AGREE, DEFAULT_INFO, REGION, INTEREST -> currentPage
            else -> currentPage - 1
        }
    )
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

//TODO REFACTOR
@BindingAdapter("currentPage", "isCheckedTermsConditions", "isCheckedPersonalInfo"
    , "nameInputText", "isNameError",
    "nickNameInputText", "isNicknameError", "isNicknameOverlap",
    "birthText", "isFemaleRadioButtonChecked", "isMaleRadioButtonChecked", "selectedDetailRegionText")
fun setAgreeButtonEnabled(
    button: Button,
    currentPage: Int,
    isCheckedTermsConditions: Boolean,
    isCheckedPersonalInfo: Boolean,
    nameInputText: String,
    isNameError: Boolean,
    nickNameInputText: String,
    isNicknameError: Boolean,
    isNicknameOverlap: Boolean,
    birthText: String,
    isFemaleRadioButtonChecked: Boolean,
    isMaleRadioButtonChecked : Boolean,
    selectedDetailRegionText: String
) {
    button.isEnabled = (currentPage == AGREE && isCheckedPersonalInfo && isCheckedTermsConditions) ||
            (currentPage == DEFAULT_INFO &&
                    nameInputText.isNotBlank() && !isNameError
                    && nickNameInputText.isNotBlank() && !isNicknameError && !isNicknameOverlap
                    && birthText.isNotBlank()
                    && isFemaleRadioButtonChecked != isMaleRadioButtonChecked) ||
            (currentPage == REGION && selectedDetailRegionText.isNotEmpty()) ||
            (currentPage == INTEREST) || (currentPage == REGISTRATION)

    if(currentPage == REGISTRATION) {
        button.setBackgroundResource(R.drawable.bg_solid_g08_rounded_5)
        button.setTextColor(button.context.getColor(R.color.gray_g02))
    } else {
        button.setBackgroundResource(R.drawable.selector_main_button_bg)
        button.setTextColor(button.context.getColorStateList(R.color.selector_main_button_text))
    }
}