package com.finpo.app.ui.my_page

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.utils.ImageUtils
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
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

    private val _settingClickEvent = MutableSingleLiveData<Boolean>()
    val settingClickEvent: SingleLiveData<Boolean> = _settingClickEvent

    init {
        getMyInfo()
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

    private fun getMyInfo() {
        viewModelScope.launch {
            val myInfoResponse = myInfoRepository.getMyInfo()
            myInfoResponse.onSuccess {
                _nickname.value = data.data.nickname ?: ""
                _gender.value = data.data.gender ?: ""
                _profileImgUrl.value = data.data.profileImg
                _oAuthType.value = data.data.oAuthType ?: ""
            }
        }
    }
}