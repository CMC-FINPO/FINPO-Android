package com.finpo.app.ui.intro.default_info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.R
import com.finpo.app.repository.IntroRepository
import com.finpo.app.utils.MAX_NAME_LENGTH
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultInfoLiveData @Inject constructor(
    private val introRepository: IntroRepository
) : ViewModel() {
    val nameInputText = MutableLiveData<String>()

    private val _nameErrorText = MutableLiveData<Int?>()
    val nameErrorText: LiveData<Int?> = _nameErrorText

    private val _isNameError = MutableLiveData<Boolean>()
    val isNameError: LiveData<Boolean> = _isNameError

    val nickNameInputText = MutableLiveData<String>()

    private val _nickNameErrorText = MutableLiveData<Int?>()
    val nickNameErrorText: LiveData<Int?> = _nickNameErrorText

    private val _isNicknameError = MutableLiveData<Boolean>()
    val isNicknameError: LiveData<Boolean> = _isNicknameError

    private val _isNicknameOverlap = MutableLiveData<Boolean>()
    val isNicknameOverlap: LiveData<Boolean> = _isNicknameOverlap

    private val _showDatePickerDialog = MutableSingleLiveData<Boolean>()
    val showDatePickerDialog: SingleLiveData<Boolean> = _showDatePickerDialog

    private val _birthText = MutableLiveData<String>()
    val birthText: LiveData<String> = _birthText

    var lastNicknameInput = ""

    init {
        _isNicknameOverlap.value = true
    }

    fun setBirth(birth: String) {
        _birthText.value = birth
    }

    fun afterNameTextChanged() {
        nameInputText.value?.let { nameText ->
            verifyNameLength(nameText, _nameErrorText, _isNameError)
        }
    }

    fun afterNicknameTextChanged() {
        var debounceJob: Job? = null
        nickNameInputText.value?.let { nickNameText ->
            verifyNameLength(nickNameText, _nickNameErrorText, _isNicknameError)
            debounceJob?.cancel()
            if (lastNicknameInput == nickNameText) return
            lastNicknameInput = nickNameText
            debounceJob = viewModelScope.launch {
                delay(500L)

                if (!(lastNicknameInput == nickNameText && nickNameText.isNotBlank())) {
                    _isNicknameOverlap.value = true
                    return@launch
                }

                val data = introRepository.checkNicknameDuplication(nickNameText)

                if (!data.isSuccessful) return@launch
                val isOverlap = data.body()?.data ?: return@launch

                if (isOverlap) {
                    _nickNameErrorText.value = R.string.is_overlap_nickname
                    _isNicknameError.value = true
                } else if (!isOverlap && _isNicknameError.value == false) _nickNameErrorText.value = null

                _isNicknameOverlap.value = isOverlap
            }

        }
    }

    private fun verifyNameLength(
        text: String,
        errorText: MutableLiveData<Int?>,
        isError: MutableLiveData<Boolean>
    ) {
        if (text.length > MAX_NAME_LENGTH) {
            errorText.value = R.string.please_input_under_13
            isError.value = true
        } else {
            errorText.value = null
            isError.value = false
        }
    }

    fun showDialog() {
        _showDatePickerDialog.setValue(true)
    }
}