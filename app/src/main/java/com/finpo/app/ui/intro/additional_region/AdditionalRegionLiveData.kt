package com.finpo.app.ui.intro.additional_region

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finpo.app.model.remote.RegionResponse
import com.finpo.app.repository.IntroRepository
import com.finpo.app.ui.intro.living_area.LivingAreaLiveData
import com.finpo.app.utils.MAX_ADDITIONAL_COUNT
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class AdditionalRegionLiveData@Inject constructor(
    val livingAreaLiveData: LivingAreaLiveData,
    private val introRepository: IntroRepository
): ViewModel() {
    private val _additionalRegionData = MutableLiveData<RegionResponse>()
    val additionalRegionData: LiveData<RegionResponse> = _additionalRegionData

    private val _additionalRegionSel = MutableLiveData<Int>()
    val additionalRegionSel: LiveData<Int> = _additionalRegionSel

    private val _additionalRegionSelEvent = MutableSingleLiveData<Int>()
    val additionalRegionSelEvent: SingleLiveData<Int> = _additionalRegionSelEvent

    private val _additionalRegionDetailData = MutableLiveData<RegionResponse>()
    val additionalRegionDetailData: LiveData<RegionResponse> = _additionalRegionDetailData

    private val _additionalDetailRegionSelTextList = MutableLiveData<MutableList<String>>()
    val additionalDetailRegionSelTextList: LiveData<MutableList<String>> = _additionalDetailRegionSelTextList

    private val _additionalDetailRegionSelCount = MutableLiveData<Int>()
    val additionalDetailRegionSelCount: LiveData<Int> = _additionalDetailRegionSelCount

    private val _chooseMaxToastEvent = MutableSingleLiveData<Boolean>()
    val chooseMaxToastEvent: SingleLiveData<Boolean> = _chooseMaxToastEvent

    private val _regionOverlapToastEvent = MutableSingleLiveData<Boolean>()
    val regionOverlapToastEvent: SingleLiveData<Boolean> = _regionOverlapToastEvent

    private val additionalRegionName = MutableLiveData<String>()

    init {
        _additionalDetailRegionSelCount.value = 0
        _additionalDetailRegionSelTextList.value = MutableList(5){""}
        additionalRegionName.value = ""
    }

    fun setAdditionalRegionData(regionResponse: RegionResponse) {
        _additionalRegionData.value = regionResponse
    }

    fun setAdditionalRegionSel(regionId: Int) {
        _additionalRegionSel.value = regionId
        _additionalRegionSelEvent.setValue(regionId)
        livingAreaLiveData.setRegionName(additionalRegionName, regionId)
        livingAreaLiveData.getRegionDetail(regionId, additionalRegionName ,_additionalRegionDetailData)
    }

    fun selectAdditionalRegion(regionId: Int) {
        if(regionId == _additionalRegionSel.value) return
        setAdditionalRegionSel(regionId)
    }

    fun selectAdditionalRegionDetail(additionalRegionDetailId: Int, additionalRegionDetailText: String) {
        Log.d("addRegion","${_additionalRegionSel.value!!} $additionalRegionDetailId")
        if(_additionalDetailRegionSelCount.value!! >= MAX_ADDITIONAL_COUNT) {
            _chooseMaxToastEvent.setValue(true)
            return
        }
        val regionDetailTextFormatted = if(additionalRegionDetailId == _additionalRegionSel.value!!)  additionalRegionDetailText
        else "${additionalRegionName.value!!} $additionalRegionDetailText"
        if(regionDetailTextFormatted in _additionalDetailRegionSelTextList.value!!) {
            _regionOverlapToastEvent.setValue(true)
            return
        }
        _additionalDetailRegionSelTextList.value!![_additionalDetailRegionSelCount.value!!] = regionDetailTextFormatted
        _additionalDetailRegionSelTextList.value = _additionalDetailRegionSelTextList.value!!
        _additionalDetailRegionSelCount.value = _additionalDetailRegionSelCount.value!! + 1
    }

    fun deleteAdditionalRegionDetail(deleteIndex: Int) {
        val detailRegionTextList = _additionalDetailRegionSelTextList.value!!
        for(i in deleteIndex until MAX_ADDITIONAL_COUNT - 1) {
            detailRegionTextList[i] = detailRegionTextList[i + 1]
        }
        detailRegionTextList[MAX_ADDITIONAL_COUNT - 1] = ""
        _additionalDetailRegionSelTextList.value = detailRegionTextList
        _additionalDetailRegionSelCount.value = _additionalDetailRegionSelCount.value!! - 1
    }
}