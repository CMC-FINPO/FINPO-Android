package com.finpo.app.ui.intro.additional_region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finpo.app.model.remote.RegionResponse
import com.finpo.app.repository.IntroRepository
import com.finpo.app.ui.intro.living_area.LivingAreaLiveData
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

    private val regionName = MutableLiveData<String>()

    init {
        regionName.value = ""
    }

    fun setAdditionalRegionData(regionResponse: RegionResponse) {
        _additionalRegionData.value = regionResponse
    }

    fun setAdditionalRegionSel(regionId: Int) {
        _additionalRegionSel.value = regionId
        _additionalRegionSelEvent.setValue(regionId)
        livingAreaLiveData.setRegionName(regionName, regionId)
        livingAreaLiveData.getRegionDetail(regionId, regionName ,_additionalRegionDetailData)
    }

    fun selectAdditionalRegion(regionId: Int) {
        if(regionId == _additionalRegionSel.value) return
        setAdditionalRegionSel(regionId)
    }
}