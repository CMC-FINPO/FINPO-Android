package com.finpo.app.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.repository.IntroRepository
import com.finpo.app.ui.intro.default_info.DefaultInfoLiveData
import com.finpo.app.ui.intro.living_area.LivingAreaLiveData
import com.finpo.app.ui.intro.login.LoginLiveData
import com.finpo.app.ui.intro.terms_conditions.TermsConditionsLiveData
import com.finpo.app.utils.*
import com.finpo.app.utils.PAGE.INTEREST
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
    private val introRepository: IntroRepository
) : ViewModel() {

    private val _currentPage = MutableLiveData<Int>()
    val currentPage: LiveData<Int> = _currentPage

    private val _registerErrorToastEvent = MutableSingleLiveData<Boolean>()
    val registerErrorToastEvent: SingleLiveData<Boolean> = _registerErrorToastEvent

    init {
        _currentPage.value = 0
    }

    private fun registerByKakao() {
        viewModelScope.launch {
            val textHashMap = getUserInputInfo()
            val bitmapMultipartBody: MultipartBody.Part? = ImageUtils().getProfileImgFromBitmap(loginLiveData.profileImage)
            val data = introRepository.registerByKakao(loginLiveData.acToken, bitmapMultipartBody, textHashMap)
            if(data.isSuccessful)   _currentPage.value = _currentPage.value?.plus(1)
            else _registerErrorToastEvent.setValue(true)
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


    fun nextPage() {
        //TODO 마지막 페이지인 경우 예외 처리 필요
        if(_currentPage.value == INTEREST) registerByKakao()
        else _currentPage.value = _currentPage.value?.plus(1)
    }

    fun prevPage() {
        _currentPage.value = _currentPage.value?.minus(1)
    }
}