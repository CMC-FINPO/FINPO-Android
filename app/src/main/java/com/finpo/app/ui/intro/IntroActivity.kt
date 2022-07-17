package com.finpo.app.ui.intro

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.finpo.app.R
import com.finpo.app.databinding.ActivityIntroBinding
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseActivity
import com.finpo.app.utils.PAGE
import com.finpo.app.utils.PAGE.FINISH
import com.finpo.app.utils.PAGE.INTEREST
import com.finpo.app.utils.PAGE.LOGIN
import com.finpo.app.utils.PAGE.REGISTRATION
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro) {
    private lateinit var viewPager: ViewPager2
    val viewModel by viewModels<IntroViewModel>()

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewPager = binding.vpIntro
        viewPager.adapter = IntroAdapter(this)
        viewPager.isUserInputEnabled = false

        viewModel.registerSuccessEvent.observe(this) {
            viewModel.setNotification(true)
            hideLoadingDialog()
            viewModel.nextPage()
        }

        viewModel.registerErrorToastEvent.observe(this) {
            shortShowToast("회원가입 실패!")
        }

        viewModel.introMainButtonClickEvent.observe(this) {
            when (viewModel.currentPage.value) {
                INTEREST -> {
                    showLoadingDialog()
                    if(viewModel.loginLiveData.oAuthType == getString(R.string.kakao_eng))
                        viewModel.registerByKakao()
                    else
                        viewModel.registerByGoogle()
                }
                REGISTRATION -> viewModel.goToLastPage()
                FINISH -> viewModel.postAdditionalInfo()
                else -> viewModel.nextPage()
            }
        }

        viewModel.goToMainActivityEvent.observe(this) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        when (viewModel.currentPage.value) {
            LOGIN, REGISTRATION -> doDelayFinish()
            else -> viewModel.prevPage()
        }
    }
}