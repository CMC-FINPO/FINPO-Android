package com.finpo.app.ui.interest_setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.CategoryChildFormat
import com.finpo.app.model.remote.StatusPurpose
import com.finpo.app.repository.CategoryRepository
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.repository.StatusPurposeRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestSettingViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val myInfoRepository: MyInfoRepository,
    private val statusPurposeRepository: StatusPurposeRepository
): ViewModel() {
    private val _interestCategoryData = MutableLiveData<List<CategoryChildFormat>>()
    val interestCategoryData: LiveData<List<CategoryChildFormat>> = _interestCategoryData

    private val _userCategoryData = MutableLiveData<IntArray>()
    val userCategoryData: LiveData<IntArray> = _userCategoryData

    private val _purposeData = MutableLiveData<List<StatusPurpose>>()
    val purposeData: LiveData<List<StatusPurpose>> = _purposeData

    init {
        initData()
        getPurposeData()
    }

    private fun initData() {
        viewModelScope.launch {
            val myCategoryResponse = myInfoRepository.getMyCategory()
            if(myCategoryResponse !is ApiResponse.Success)  return@launch
            _userCategoryData.value = IntArray(myCategoryResponse.data.data.size) { myCategoryResponse.data.data[it].category.id }

            val categoryResponse = categoryRepository.getCategoryChildFormat()
            if(categoryResponse !is ApiResponse.Success)    return@launch
            _interestCategoryData.value = categoryResponse.data.data
        }
    }

    private fun getPurposeData() {
        viewModelScope.launch {
            val purposeResponse = statusPurposeRepository.getPurposeList()
            purposeResponse.onSuccess {
                _purposeData.value = data.data
            }
        }
    }
}