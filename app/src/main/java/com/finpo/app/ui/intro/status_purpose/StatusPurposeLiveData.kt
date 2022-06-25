package com.finpo.app.ui.intro.status_purpose

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.StatusPurpose
import com.finpo.app.repository.StatusPurposeRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class StatusPurposeLiveData @Inject constructor(
    private val statusPurposeRepository: StatusPurposeRepository
): ViewModel() {
    private val _statusData = MutableLiveData<List<StatusPurpose>>()
    val statusData: LiveData<List<StatusPurpose>> = _statusData

    private val _statusClickEvent = MutableSingleLiveData<Boolean>()
    val statusClickEvent: SingleLiveData<Boolean> = _statusClickEvent

    private val _statusSelectedId = MutableLiveData<Int>()
    val statusSelectedId: LiveData<Int> = _statusSelectedId

    private val _purposeData = MutableLiveData<List<StatusPurpose>>()
    val purposeData: LiveData<List<StatusPurpose>> = _purposeData

    private val purposeIds = mutableSetOf<Int>()

    fun setStatusData() {
        if(!_statusData.value.isNullOrEmpty()) return
        viewModelScope.launch {
            val statusResponse = statusPurposeRepository.getStatusList()
            statusResponse.onSuccess {
                _statusData.value = data.data
            }
        }
    }

    fun statusClick(id: Int) {
        _statusSelectedId.value = id
        _statusClickEvent.setValue(true)
    }

    fun setPurposeData() {
        if(!_purposeData.value.isNullOrEmpty()) return
        viewModelScope.launch {
            val purposeResponse = statusPurposeRepository.getPurposeList()
            purposeResponse.onSuccess {
                _purposeData.value = data.data
            }
        }
    }

    fun purposeClick(id: Int) {
        if(id in purposeIds)    purposeIds.remove(id)
        else purposeIds.add(id)
    }
}