package com.finpo.app.ui.intro.terms_conditions

import android.widget.CompoundButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class TermsConditionsLiveData @Inject constructor() {
    private val _isCheckedAll = MutableLiveData<Boolean>()
    val isCheckedAll: LiveData<Boolean> = _isCheckedAll

    val isCheckedTermsConditions = MutableLiveData<Boolean>()
    val isCheckedPersonalInfo = MutableLiveData<Boolean>()
    val isCheckedMarketing = MutableLiveData<Boolean>()

    fun onCheckboxAllChange(button: CompoundButton, check: Boolean) {
        _isCheckedAll.value = check
        isCheckedTermsConditions.value = check
        isCheckedPersonalInfo.value = check
        isCheckedMarketing.value = check
    }
}