package com.finpo.app.ui.intro.additional_region

import androidx.lifecycle.ViewModel
import com.finpo.app.ui.common.RegionViewModel
import com.finpo.app.ui.intro.living_area.LivingAreaLiveData
import com.finpo.app.utils.MAX_ADDITIONAL_COUNT
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class AdditionalRegionLiveData@Inject constructor(
    val livingAreaLiveData: LivingAreaLiveData,
    val additionalRegionRegionViewModel: RegionViewModel
): ViewModel() {
    init {
        additionalRegionRegionViewModel.MAX_COUNT = MAX_ADDITIONAL_COUNT
        additionalRegionRegionViewModel.setRegionTextList(MutableList(MAX_ADDITIONAL_COUNT) {""})
        additionalRegionRegionViewModel.setRegionCount(0)
    }
}