package com.finpo.app.ui.intro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.di.FinpoApplication
import com.finpo.app.model.remote.RegionResponse
import com.finpo.app.repository.AdditionalInfoRepository
import com.finpo.app.repository.IntroRepository
import com.finpo.app.ui.intro.additional_region.AdditionalRegionLiveData
import com.finpo.app.ui.intro.default_info.DefaultInfoLiveData
import com.finpo.app.ui.intro.living_area.LivingAreaLiveData
import com.finpo.app.ui.intro.login.LoginLiveData
import com.finpo.app.ui.intro.register_complete.RegisterCompleteLiveData
import com.finpo.app.ui.intro.terms_conditions.TermsConditionsLiveData
import com.finpo.app.utils.*
import com.finpo.app.utils.PAGE.FINISH
import com.finpo.app.utils.PAGE.INTEREST
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    val termsConditionsLiveData: TermsConditionsLiveData,
    val loginLiveData: LoginLiveData,
    val defaultInfoLiveData: DefaultInfoLiveData,
    val livingAreaLiveData: LivingAreaLiveData,
    val additionalRegionLiveData: AdditionalRegionLiveData,
    val registerCompleteLiveData: RegisterCompleteLiveData,
    private val introRepository: IntroRepository,
    private val additionalInfoRepository: AdditionalInfoRepository
) : ViewModel() {

    private val _currentPage = MutableLiveData<Int>()
    val currentPage: LiveData<Int> = _currentPage

    private val _prevPage = MutableLiveData<Int>()

    private val _introMainButtonClickEvent = MutableSingleLiveData<Boolean>()
    val introMainButtonClickEvent: SingleLiveData<Boolean> = _introMainButtonClickEvent

    private val _registerErrorToastEvent = MutableSingleLiveData<Boolean>()
    val registerErrorToastEvent: SingleLiveData<Boolean> = _registerErrorToastEvent

    private val _goToMainActivityEvent = MutableSingleLiveData<Boolean>()
    val goToMainActivityEvent: SingleLiveData<Boolean> = _goToMainActivityEvent

    init {
        _prevPage.value = 0
        _currentPage.value = 0
    }

    fun goToMainActivity() {
        _goToMainActivityEvent.setValue(true)
    }

    fun postAdditionalInfo() {
        viewModelScope.launch {
            val response = additionalInfoRepository.addMyInterestRegion(additionalRegionLiveData.additionalRegionDetailIdList)
            response.onSuccess {
                goToMainActivity()
            }
        }
    }

    fun registerByKakao() {
        viewModelScope.launch {
            val textHashMap = getUserInputInfo()
            val bitmapMultipartBody: MultipartBody.Part? =
                ImageUtils().getProfileImgFromBitmap(loginLiveData.profileImage)
            val registerKakaoResponse = introRepository.registerByKakao(
                loginLiveData.acToken,
                bitmapMultipartBody,
                textHashMap
            )

            if (registerKakaoResponse is ApiResponse.Success) {
                FinpoApplication.encryptedPrefs.saveAccessToken(registerKakaoResponse.data.data.accessToken ?: "")
                FinpoApplication.encryptedPrefs.saveRefreshToken(registerKakaoResponse.data.data.refreshToken ?: "")
                nextPage()
            } else {
                _registerErrorToastEvent.setValue(true)
            }
        }
    }

    private fun getUserInputInfo(): HashMap<String, RequestBody> {
        val name = defaultInfoLiveData.nameInputText.value.toString().toPlainRequestBody()
        val nickname =
            defaultInfoLiveData.nickNameInputText.value.toString().toPlainRequestBody()
        val birth = defaultInfoLiveData.birthText.value.toString().toPlainRequestBody()
        val gender = defaultInfoLiveData.gender.toPlainRequestBody()
        val regionId = livingAreaLiveData.regionDetailSel.value.toString().toPlainRequestBody()
        //TODO 수정
        val status = "대학교 재학 중".toPlainRequestBody()
        return hashMapOf(
            "name" to name, "nickname" to nickname, "birth" to birth, "gender" to gender,
            "regionId" to regionId, "status" to status
        )
    }

    fun goToLastPage() {
        _prevPage.value = _currentPage.value
        _currentPage.value = FINISH
    }

    fun introMainButtonClick() {
        _introMainButtonClickEvent.setValue(true)
    }

    fun nextPage() {
        _prevPage.value = _currentPage.value
        _currentPage.value = _currentPage.value?.plus(1)
    }

    fun prevPage() {
        if(_prevPage.value!! < _currentPage.value!!)
            _currentPage.value = _prevPage.value
        else
            _currentPage.value = _currentPage.value?.minus(1)
    }
}