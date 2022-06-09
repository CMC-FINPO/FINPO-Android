package com.finpo.app.ui.intro.default_info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.R
import com.finpo.app.repository.IntroRepository
import com.finpo.app.utils.MAX_NAME_LENGTH
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultInfoLiveData @Inject constructor(
    private val introRepository: IntroRepository
) : ViewModel() {
    val nameInputText = MutableLiveData<String>()
    val nameFocused = MutableLiveData<Boolean>()

    private val _nameErrorText = MutableLiveData<Int?>()
    val nameErrorText: LiveData<Int?> = _nameErrorText

    fun afterNameTextChanged() {
        nameInputText.value?.let {
            _nameErrorText.value = if (it.length > MAX_NAME_LENGTH) R.string.please_input_under_13
            else null
        }
        viewModelScope.launch {
            val data = introRepository.checkNicknameDuplication(nameInputText.value ?: "")
        }
    }
}