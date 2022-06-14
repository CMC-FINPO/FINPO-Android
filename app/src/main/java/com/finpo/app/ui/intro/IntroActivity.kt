package com.finpo.app.ui.intro

import android.content.Intent
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.finpo.app.R
import com.finpo.app.databinding.ActivityIntroBinding
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseActivity
import com.finpo.app.utils.PAGE
import com.finpo.app.utils.PAGE.INTEREST
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
                startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                finish()
            }
        }

        viewModel.registerErrorToastEvent.observe(this) {
            shortShowToast("회원가입 실패!")
        }

        viewModel.introMainButtonClickEvent.observe(this) {
            //TODO 마지막 페이지인 경우 예외 처리 필요
            when (viewModel.currentPage.value) {
                INTEREST -> viewModel.registerByKakao()
                REGISTRATION -> goToMainActivity()
                else -> viewModel.nextPage()
            }
        }

        //TODO finpo refresh token이 있는 경우 바로 main activity로 이동
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        when (viewModel.currentPage.value) {
            LOGIN, REGISTRATION -> doDelayFinish()
            else -> viewModel.prevPage()
        }
    }
}