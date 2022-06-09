package com.finpo.app.ui.intro.default_info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.finpo.app.R
import com.finpo.app.utils.MAX_NAME_LENGTH
import javax.inject.Inject

class DefaultInfoLiveData @Inject constructor() {
    val nameInputText = MutableLiveData<String>()
    val nameFocused = MutableLiveData<Boolean>()

    private val _nameErrorText = MutableLiveData<Int?>()
    val nameErrorText: LiveData<Int?> = _nameErrorText

    fun afterNameTextChanged() {
        nameInputText.value?.let {
            _nameErrorText.value = if (it.length > MAX_NAME_LENGTH) R.string.please_input_under_13
            else null
        }
    }
}