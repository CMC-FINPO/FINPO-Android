package com.finpo.app.ui.intro

import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.finpo.app.R
import com.finpo.app.databinding.ActivityIntroBinding
import com.finpo.app.ui.common.BaseActivity
import com.finpo.app.utils.PAGE.LOGIN
import com.finpo.app.utils.PAGE.REGISTRATION
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

        viewModel.loginLiveData.isLoginSuccessfulEvent.observe(this) { isSuccessful ->
            if (!isSuccessful) {
                viewModel.nextPage()
            } else {
                viewModel.loginLiveData.acToken = ""
                //TODO 메인 페이지 이동
            }
        }

        viewModel.registerErrorToastEvent.observe(this) {
            shortShowToast("회원가입 실패!")
        }

        //TODO finpo refresh token이 있는 경우 바로 main activity로 이동
    }

    override fun onBackPressed() {
        when(viewModel.currentPage.value) {
            LOGIN, REGISTRATION -> doDelayFinish()
            else -> viewModel.prevPage()
        }
    }
}