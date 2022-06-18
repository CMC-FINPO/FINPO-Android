package com.finpo.app.ui.my_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.repository.MyInfoRepository
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository
): ViewModel() {
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    private val _gender = MutableLiveData<String>()
    val gender: LiveData<String> = _gender

    private val _profileImgUrl = MutableLiveData<String?>()
    val profileImgUrl: LiveData<String?> = _profileImgUrl

    init {
        getMyInfo()
    }

    private fun getMyInfo() {
        viewModelScope.launch {
            val myInfoResponse = myInfoRepository.getMyInfo()
            myInfoResponse.onSuccess {
                _nickname.value = data.data.nickname ?: ""
                _gender.value = data.data.gender ?: ""
                _profileImgUrl.value = data.data.profileImg
            }
        }
    }
}