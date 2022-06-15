package com.finpo.app.ui.intro.register_complete

import androidx.lifecycle.MutableLiveData
import com.finpo.app.model.remote.RegionResponse
import com.finpo.app.ui.intro.additional_region.AdditionalRegionLiveData
import com.finpo.app.ui.intro.living_area.LivingAreaLiveData
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import javax.inject.Inject

class RegisterCompleteLiveData @Inject constructor(
    val additionalRegionLiveData: AdditionalRegionLiveData,
    val livingAreaLiveData: LivingAreaLiveData
) {
    private val _additionalInfoButtonEvent = MutableSingleLiveData<Boolean>()
    val additionalInfoButtonEvent: SingleLiveData<Boolean> = _additionalInfoButtonEvent

    fun additionalInfoButton() {
        additionalRegionLiveData.setAdditionalRegionData(livingAreaLiveData.regionData.value ?: RegionResponse(listOf()))
        additionalRegionLiveData.setAdditionalRegionSel(livingAreaLiveData.regionData.value?.data?.get(0)?.id ?: 0)
        _additionalInfoButtonEvent.setValue(true)
    }
}