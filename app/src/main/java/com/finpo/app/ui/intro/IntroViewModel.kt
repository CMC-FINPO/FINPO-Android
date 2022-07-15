package com.finpo.app.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.di.FinpoApplication
import com.finpo.app.model.remote.TokenResponse
import com.finpo.app.repository.EditRegionRepository
import com.finpo.app.repository.IntroRepository
import com.finpo.app.repository.NotificationRepository
import com.finpo.app.repository.StatusPurposeRepository
import com.finpo.app.ui.intro.additional_region.AdditionalRegionLiveData
import com.finpo.app.ui.intro.default_info.DefaultInfoLiveData
import com.finpo.app.ui.intro.interest.InterestLiveData
import com.finpo.app.ui.intro.living_area.LivingAreaLiveData
import com.finpo.app.ui.intro.login.LoginLiveData
import com.finpo.app.ui.intro.register_complete.RegisterCompleteLiveData
import com.finpo.app.ui.intro.status_purpose.StatusPurposeLiveData
import com.finpo.app.ui.intro.terms_conditions.TermsConditionsLiveData
import com.finpo.app.utils.*
import com.finpo.app.utils.PAGE.FINISH
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class IntroViewModel @Inject constructor(
    val termsConditionsLiveData: TermsConditionsLiveData,
    val loginLiveData: LoginLiveData,
    val defaultInfoLiveData: DefaultInfoLiveData,
    val livingAreaLiveData: LivingAreaLiveData,
    val additionalRegionLiveData: AdditionalRegionLiveData,
    val registerCompleteLiveData: RegisterCompleteLiveData,
    val interestLiveData: InterestLiveData,
    val statusPurposeLiveData: StatusPurposeLiveData,
    private val introRepository: IntroRepository,
    private val editRegionRepository: EditRegionRepository,
    private val statusPurposeRepository: StatusPurposeRepository,
    private val notificationRepository: NotificationRepository
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

    private val _registerSuccessEvent = MutableSingleLiveData<Boolean>()
    val registerSuccessEvent: SingleLiveData<Boolean> = _registerSuccessEvent

    init {
        _prevPage.value = 0
        _currentPage.value = 0
    }

    private fun goToMainActivity() {
        _goToMainActivityEvent.setValue(true)
    }

    fun setNotification(subscribe: Boolean?) {
        viewModelScope.launch {
            val response = notificationRepository.setNotification(getTokenResult(), subscribe)
            if(subscribe == null) return@launch
            response.suspendOnSuccess {
                notificationRepository.putMyNotification(adSubscribe = termsConditionsLiveData.isCheckedMarketing.value)
            }
        }
    }

    private suspend fun getTokenResult() = suspendCoroutine<String?> { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) continuation.resume(it.result)
             else continuation.resume(null)
        }
    }

    fun postAdditionalInfo() {
        viewModelScope.launch {
            val additionalResponse = editRegionRepository.editMyInterestRegion(additionalRegionLiveData.additionalRegionDetailIdList)
            val statusPurposeResponse = statusPurposeRepository.setStatusPurpose(
                statusPurposeLiveData.statusSelectedId.value,
                statusPurposeLiveData.purposeIds.value?.toList()
            )
            if((additionalResponse !is ApiResponse.Success) || (statusPurposeResponse !is ApiResponse.Success)) return@launch
            goToMainActivity()
        }
    }

    fun registerByKakao() {
        viewModelScope.launch {
            val registerKakaoResponse = introRepository.registerByKakao(
                loginLiveData.acToken,
                ImageUtils().getProfileImgFromBitmap(loginLiveData.profileImage),
                getUserInputInfo()
            )
            processRegisterResponse(registerKakaoResponse)
        }
    }

    private fun processRegisterResponse(registerResponse: ApiResponse<TokenResponse>) {
        if (registerResponse is ApiResponse.Success) {
            FinpoApplication.encryptedPrefs.saveAccessToken(registerResponse.data.data.accessToken ?: "")
            FinpoApplication.encryptedPrefs.saveRefreshToken(registerResponse.data.data.refreshToken ?: "")
            _registerSuccessEvent.setValue(true)
        } else {
            _registerErrorToastEvent.setValue(true)
        }
    }

    fun registerByGoogle() {
        viewModelScope.launch {
            val registerGoogleResponse = introRepository.registerByGoogle(
                loginLiveData.acToken,
                ImageUtils().getProfileImgFromBitmap(loginLiveData.profileImage),
                getUserInputInfo()
            )
            processRegisterResponse(registerGoogleResponse)
        }
    }

    private fun getUserInputInfo(): HashMap<String, RequestBody> {
        val name = defaultInfoLiveData.nameInputText.value.toString().toPlainRequestBody()
        val nickname =
            defaultInfoLiveData.nickNameInputText.value.toString().toPlainRequestBody()
        val birth = defaultInfoLiveData.birthText.value.toString().toPlainRequestBody()
        val gender = defaultInfoLiveData.gender.toPlainRequestBody()
        val regionId = livingAreaLiveData.regionDetailSel.value.toString().toPlainRequestBody()
        val categories = Gson().toJson(interestLiveData.userInterestData.value?.toList())
        return hashMapOf(
            "name" to name, "nickname" to nickname, "birth" to birth, "gender" to gender,
            "regionId" to regionId, "categories" to categories.toPlainRequestBody()
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