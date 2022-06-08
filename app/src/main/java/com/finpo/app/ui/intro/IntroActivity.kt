package com.finpo.app.ui.intro

import android.util.Log
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.finpo.app.R
import com.finpo.app.databinding.ActivityIntroBinding
import com.finpo.app.ui.common.BaseActivity
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro) {
    lateinit var viewPager: ViewPager2
    val viewModel by viewModels<IntroViewModel>()

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewPager = binding.vpIntro
        viewPager.adapter = IntroAdapter(this)
        viewPager.isUserInputEnabled = false

        //TODO finpo refresh token이 있는 경우 바로 main activity로 이동
    }

    override fun onBackPressed() {
        when(viewModel.currentPage.value) {
            0 -> doDelayFinish()
            else -> viewModel.prevPage()
        }
    }
}