package com.finpo.app.ui.intro

import android.util.Log
import android.widget.CompoundButton
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.repository.IntroRepository
import com.finpo.app.ui.intro.default_info.DefaultInfoLiveData
import com.finpo.app.ui.intro.living_area.LivingAreaLiveData
import com.finpo.app.ui.intro.login.LoginLiveData
import com.finpo.app.ui.intro.terms_conditions.TermsConditionsLiveData
import com.finpo.app.utils.BitmapRequestBody
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.PAGE.INTEREST
import com.finpo.app.utils.SingleLiveData
import com.finpo.app.utils.toPlainRequestBody
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

    init {
        _currentPage.value = 0
    }

    //TODO 함수 INTEREST로 이동
    private fun registerByKakao() {
        viewModelScope.launch {
            val name = defaultInfoLiveData.nameInputText.value.toString().toPlainRequestBody()
            val nickname = defaultInfoLiveData.nickNameInputText.value.toString().toPlainRequestBody()
            val birth = defaultInfoLiveData.birthText.value.toString().toPlainRequestBody()
            val gender = defaultInfoLiveData.gender.toPlainRequestBody()
            val regionId = livingAreaLiveData.regionDetailSel.value.toString().toPlainRequestBody()
            //TODO 수정
            val status = "대학교 재학 중".toPlainRequestBody()
            val textHashMap = hashMapOf<String, RequestBody>()
            textHashMap["name"] = name
            textHashMap["nickname"] = nickname
            textHashMap["birth"] = birth
            textHashMap["gender"] = gender
            textHashMap["regionId"] = regionId
            textHashMap["status"] = status
            val profileImage = loginLiveData.profileImage?.let { BitmapRequestBody(it) }
            val bitmapMultipartBody: MultipartBody.Part? =
                if (profileImage == null) null
                else MultipartBody.Part.createFormData("profileImgFile", "imagefile.jpeg", profileImage)

            val data = introRepository.registerByKakao(loginLiveData.acToken,bitmapMultipartBody, textHashMap)
            //TODO 실패 로직 추가
        }
    }

    fun nextPage() {
        //TODO 마지막 페이지, 중간 회원가입 완료 페이지인 경우 예외 처리 필요
        if(_currentPage.value == INTEREST) {
            registerByKakao()
        }
        _currentPage.value = _currentPage.value?.plus(1)
    }

    fun prevPage() {
        _currentPage.value = _currentPage.value?.minus(1)
    }
}