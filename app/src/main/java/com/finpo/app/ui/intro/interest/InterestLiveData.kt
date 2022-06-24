package com.finpo.app.ui.intro.interest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.ParentCategoryResponse
import com.finpo.app.model.remote.Region
import com.finpo.app.model.remote.RegionResponse
import com.finpo.app.repository.IntroRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class InterestLiveData@Inject constructor(
    private val introRepository: IntroRepository
) : ViewModel() {
    private val _categoryData = MutableLiveData<ParentCategoryResponse>()
    val categoryData: LiveData<ParentCategoryResponse> = _categoryData

    init {
        getParentCategoryData()
    }

    private fun getParentCategoryData() {
        viewModelScope.launch {
            val parentCategoryResponse = introRepository.getParentCategory()
            parentCategoryResponse.onSuccess {
                _categoryData.value = data!!
                Log.d("interest","${_categoryData.value}")
            }
        }
    }
}