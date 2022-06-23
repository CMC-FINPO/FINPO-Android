package com.finpo.app.ui.intro.terms_conditions

import android.util.Log
import android.widget.CompoundButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.finpo.app.utils.addSourceList
import javax.inject.Inject

class TermsConditionsLiveData @Inject constructor() {
    private val _isCheckedAll = MutableLiveData<Boolean>()
    val isCheckedAll: LiveData<Boolean> = _isCheckedAll

    private val _checkedAllClickEvent = MutableSingleLiveData<Boolean>()
    val checkedAllClickEvent: SingleLiveData<Boolean> = _checkedAllClickEvent

    private val _isCheckedTermsConditions = MutableLiveData<Boolean>()
    val isCheckedTermsConditions: LiveData<Boolean> = _isCheckedTermsConditions

    private val _isCheckedPersonalInfo = MutableLiveData<Boolean>()
    val isCheckedPersonalInfo: LiveData<Boolean> = _isCheckedPersonalInfo

    private val _isCheckedMarketing = MutableLiveData<Boolean>()
    val isCheckedMarketing: LiveData<Boolean> = _isCheckedMarketing

    val isTermsConditionsButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSourceList(_isCheckedTermsConditions, _isCheckedPersonalInfo) {
            isTermsConditionsValid()
        }
    }

    private fun isTermsConditionsValid(): Boolean = _isCheckedPersonalInfo.value == true && _isCheckedTermsConditions.value == true

    init {
        _isCheckedAll.value = false
        _isCheckedTermsConditions.value = false
        _isCheckedPersonalInfo.value = false
        _isCheckedMarketing.value = false
    }

    fun clickCheckboxAll(check: Boolean) {
        _checkedAllClickEvent.setValue(check)
    }

    fun onCheckboxAllChange(button: CompoundButton, check: Boolean) {
        _isCheckedAll.value = check
    }

    fun changeOtherCheckBox(check: Boolean) {
        _isCheckedTermsConditions.value = check
        _isCheckedPersonalInfo.value = check
        _isCheckedMarketing.value = check
    }

    fun onCheckboxTermsConditionsChange(button: CompoundButton, check: Boolean) {
        _isCheckedTermsConditions.value = check
        changeIsCheckedAll()
    }

    fun onCheckboxPersonalInfoChange(button: CompoundButton, check: Boolean) {
        _isCheckedPersonalInfo.value = check
        changeIsCheckedAll()
    }

    fun onCheckboxMarketingChange(button: CompoundButton, check: Boolean) {
        _isCheckedMarketing.value = check
        changeIsCheckedAll()
    }

    private fun changeIsCheckedAll() {
        _isCheckedAll.value = (_isCheckedTermsConditions.value!! &&
                _isCheckedPersonalInfo.value!! &&
                _isCheckedMarketing.value!!)
    }
}