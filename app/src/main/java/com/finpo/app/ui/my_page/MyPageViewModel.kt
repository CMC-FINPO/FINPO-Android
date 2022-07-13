package com.finpo.app.ui.my_page

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.ParentCategory
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.utils.ImageUtils
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository
): ViewModel() {
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    private val _gender = MutableLiveData<String>()
    val gender: LiveData<String> = _gender

    private val _oAuthType = MutableLiveData<String>()
    val oAuthType: LiveData<String> = _oAuthType

    private val _profileImgUrl = MutableLiveData<String?>()
    val profileImgUrl: LiveData<String?> = _profileImgUrl

    private val _profileEditClickEvent = MutableSingleLiveData<Boolean>()
    val profileEditClickEvent: SingleLiveData<Boolean> = _profileEditClickEvent

    private val _interestSettingClickEvent = MutableSingleLiveData<Boolean>()
    val interestSettingClickEvent: SingleLiveData<Boolean> = _interestSettingClickEvent

    private val _settingClickEvent = MutableSingleLiveData<Boolean>()
    val settingClickEvent: SingleLiveData<Boolean> = _settingClickEvent

    private val _alarmClickEvent = MutableSingleLiveData<Boolean>()
    val alarmClickEvent: SingleLiveData<Boolean> = _alarmClickEvent

    private val _regionClickEvent = MutableSingleLiveData<Boolean>()
    val regionClickEvent: SingleLiveData<Boolean> = _regionClickEvent

    private val _interestList = MutableLiveData<List<ParentCategory>>()
    val interestList: LiveData<List<ParentCategory>> = _interestList

    private val _participationClickEvent = MutableSingleLiveData<Boolean>()
    val participationClickEvent: SingleLiveData<Boolean> = _participationClickEvent

    init {
        _nickname.value = ""
        getMyInfo()
    }

    private fun getMyInfo() {
        viewModelScope.launch {
            val myInfoResponse = myInfoRepository.getMyInfo()
            if(myInfoResponse !is ApiResponse.Success) return@launch
            val myInfoData = myInfoResponse.data.data
            _nickname.value = myInfoData.nickname ?: ""
            _gender.value = myInfoData.gender ?: ""
            _profileImgUrl.value = myInfoData.profileImg
            _oAuthType.value = myInfoData.oAuthType ?: ""

            val interestResponse = myInfoRepository.getMyParentCategory()
            interestResponse.onSuccess { _interestList.value = data.data }
        }
    }

    fun alarmClick() {
        _alarmClickEvent.setValue(true)
    }

    fun participationClick() {
        _participationClickEvent.setValue(true)
    }

    fun interestSettingClick() {
        _interestSettingClickEvent.setValue(true)
    }

    fun regionClick() {
        _regionClickEvent.setValue(true)
    }

    fun settingClick() {
        _settingClickEvent.setValue(true)
    }

    fun changeProfileImg(profileImage: Bitmap?) {
        viewModelScope.launch {
            val bitmapMultipartBody: MultipartBody.Part? = ImageUtils().getProfileImgFromBitmap(profileImage)
            val changeProfileImgResponse = myInfoRepository.changeProfileImg(bitmapMultipartBody)
            changeProfileImgResponse.onSuccess {
                _profileImgUrl.value = data.data.profileImg
            }
        }

    }

    fun profileEditClick() {
        _profileEditClickEvent.setValue(true)
    }
}