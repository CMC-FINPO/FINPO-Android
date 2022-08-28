package com.finpo.app.ui.intro.living_area

import androidx.lifecycle.ViewModel
import com.finpo.app.ui.common.RegionViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class LivingAreaLiveData@Inject constructor(
    val livingRegionViewModel: RegionViewModel
) : ViewModel() {

    init {
        livingRegionViewModel.MAX_COUNT = 1
        livingRegionViewModel.setRegionTextList(MutableList(1) {""})
        livingRegionViewModel.setRegionCount(0)
        livingRegionViewModel.getRegionDataByRemote()
    }
}