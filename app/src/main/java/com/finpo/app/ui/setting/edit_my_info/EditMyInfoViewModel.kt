package com.finpo.app.ui.setting.edit_my_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.ui.common.BaseViewModel
import com.finpo.app.ui.intro.default_info.DefaultInfoLiveData
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMyInfoViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
    val defaultInfoLiveData: DefaultInfoLiveData
) : BaseViewModel() {
    private var defaultNickname = ""
    private val _editMyInfoSuccessfulEvent = MutableSingleLiveData<Boolean>()
    val editMyInfoSuccessfulEvent: SingleLiveData<Boolean> = _editMyInfoSuccessfulEvent

    init {
        getMyInfo()
    }

    private fun getMyInfo() {
        viewModelScope.launch {
            val response = myInfoRepository.getMyInfo()
            response.onSuccess {
                defaultInfoLiveData.nameInputText.value = data.data.name
                defaultNickname = data.data.nickname ?: ""
                defaultInfoLiveData.nickNameInputText.value = data.data.nickname
                defaultInfoLiveData._birthText.value = data.data.birth
                when (data.data.gender) {
                    "MALE" -> defaultInfoLiveData.isMaleRadioButtonChecked.value = true
                    else -> defaultInfoLiveData.isFemaleRadioButtonChecked.value = true
                }
            }
        }
    }

    fun afterNicknameTextChanged() {
        if (defaultInfoLiveData.nickNameInputText.value == defaultNickname) {
            defaultInfoLiveData._isNicknameOverlap.value = false
            defaultInfoLiveData._isNicknameError.value = false
            return
        }

        defaultInfoLiveData.afterNicknameTextChanged()
    }

    fun editClick() {
        viewModelScope.launch {
            with(defaultInfoLiveData) {
                val response = myInfoRepository.editMyInfo(
                    nameInputText.value,
                    if(nickNameInputText.value == defaultNickname) null else nickNameInputText.value,
                    birthText.value,
                    if(isMaleRadioButtonChecked.value == true) "MALE" else "FEMALE"
                )

                response.onSuccess {
                    _editMyInfoSuccessfulEvent.setValue(true)
                }
            }
        }
    }
}