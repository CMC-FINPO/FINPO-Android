package com.finpo.app.ui.intro.terms_conditions

import android.widget.CompoundButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class TermsConditionsLiveData @Inject constructor() {
    private val _isCheckedAll = MutableLiveData<Boolean>()
    val isCheckedAll: LiveData<Boolean> = _isCheckedAll

    private val _isCheckedTermsConditions = MutableLiveData<Boolean>()
    val isCheckedTermsConditions: LiveData<Boolean> = _isCheckedTermsConditions

    private val _isCheckedPersonalInfo = MutableLiveData<Boolean>()
    val isCheckedPersonalInfo: LiveData<Boolean> = _isCheckedPersonalInfo

    private val _isCheckedMarketing = MutableLiveData<Boolean>()
    val isCheckedMarketing: LiveData<Boolean> = _isCheckedMarketing

    fun onCheckboxAllChange(button: CompoundButton, check: Boolean) {
        _isCheckedAll.value = check
        _isCheckedTermsConditions.value = check
        _isCheckedPersonalInfo.value = check
        _isCheckedMarketing.value = check
    }

    fun onCheckboxTermsConditionChange(button: CompoundButton, check: Boolean) {
        _isCheckedTermsConditions.value = check
    }

    fun onCheckboxPersonalInfoChange(button: CompoundButton, check: Boolean) {
        _isCheckedPersonalInfo.value = check
    }

    fun onCheckboxMarketingChange(button: CompoundButton, check: Boolean) {
        _isCheckedMarketing.value = check
    }
}