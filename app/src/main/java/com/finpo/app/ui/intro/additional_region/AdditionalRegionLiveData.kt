package com.finpo.app.ui.intro.additional_region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finpo.app.model.remote.RegionResponse
import com.finpo.app.repository.IntroRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class AdditionalRegionLiveData@Inject constructor(
    private val introRepository: IntroRepository
): ViewModel() {
    private val _additionalRegionData = MutableLiveData<RegionResponse>()
    val additionalRegionData: LiveData<RegionResponse> = _additionalRegionData

    private val _additionalRegionSel = MutableLiveData<Int>()
    val additionalRegionSel: LiveData<Int> = _additionalRegionSel

    fun setAdditionalRegionData(regionResponse: RegionResponse) {
        _additionalRegionData.value = regionResponse
    }
}