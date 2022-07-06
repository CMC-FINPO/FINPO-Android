package com.finpo.app.ui.interest_setting

import androidx.lifecycle.*
import com.finpo.app.model.remote.CategoryChildFormat
import com.finpo.app.model.remote.CategoryRequest
import com.finpo.app.model.remote.StatusPurpose
import com.finpo.app.network.ApiService
import com.finpo.app.repository.CategoryRepository
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.repository.StatusPurposeRepository
import com.finpo.app.utils.EditRegionType
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.finpo.app.utils.addSourceList
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

    private val _userCategoryData = MutableLiveData<List<CategoryRequest>>()
    val userCategoryData: LiveData<List<CategoryRequest>> = _userCategoryData

    private val _purposeData = MutableLiveData<List<StatusPurpose>>()
    val purposeData: LiveData<List<StatusPurpose>> = _purposeData

    private val _purposeIds = MutableLiveData<MutableSet<Int>>()
    val purposeIds: LiveData<MutableSet<Int>> = _purposeIds

    private val _goToMyInfoFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToMyInfoFragmentEvent: SingleLiveData<Boolean> = _goToMyInfoFragmentEvent

    private val _backEvent = MutableSingleLiveData<Boolean>()
    val backEvent: SingleLiveData<Boolean> = _backEvent

    private val _clearEvent = MutableSingleLiveData<Boolean>()
    val clearEvent: SingleLiveData<Boolean> = _clearEvent

    val isEditInterestButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSourceList(_userCategoryData, _purposeIds) {
            isEditInterestValid()
        }
    }

    private fun isEditInterestValid(): Boolean
            = !_userCategoryData.value.isNullOrEmpty() && !_purposeIds.value.isNullOrEmpty()

    init {
        initData()
        getPurposeData()
        _purposeIds.value = mutableSetOf() //TODO REFACTOR 서버에서 받아오게 수정
    }

    private fun initData() {
        viewModelScope.launch {
            val myCategoryResponse = myInfoRepository.getMyCategory()
            if(myCategoryResponse !is ApiResponse.Success)  return@launch
            _userCategoryData.value = List(myCategoryResponse.data.data.size) { CategoryRequest(myCategoryResponse.data.data[it].category.id) }

            val categoryResponse = categoryRepository.getCategoryChildFormat()
            if(categoryResponse !is ApiResponse.Success)    return@launch
            _interestCategoryData.value = categoryResponse.data.data
        }
    }

    fun categoryClick(id: Int) {
        val userCategoryData = _userCategoryData.value?.toMutableList() ?: mutableListOf()
        if (CategoryRequest(id) in userCategoryData) userCategoryData.remove(CategoryRequest(id))
        else userCategoryData.add(CategoryRequest(id))
        _userCategoryData.value = userCategoryData
    }

    private fun getPurposeData() {
        viewModelScope.launch {
            val purposeResponse = statusPurposeRepository.getPurposeList()
            purposeResponse.onSuccess {
                _purposeData.value = data.data
            }
        }
    }

    fun purposeClick(id: Int) {
        if(id in _purposeIds.value!!)    _purposeIds.value!!.remove(id)
        else _purposeIds.value!!.add(id)

        _purposeIds.value = _purposeIds.value
    }

    fun selectBtnClick() {
        viewModelScope.launch {
            val statusPurposeResponse = statusPurposeRepository.setStatusPurpose(purposeIds = purposeIds.value?.toList())
            val editMyCategoryResponse = myInfoRepository.editMyCategory(_userCategoryData.value ?: listOf())
            if(statusPurposeResponse is ApiResponse.Success && editMyCategoryResponse is ApiResponse.Success)
                _goToMyInfoFragmentEvent.setValue(true)
        }
    }

    fun backClick() {
        _backEvent.setValue(true)
    }

    fun clearClick() {
        _clearEvent.setValue(true)
    }

    fun clearData() {
        _purposeIds.value = mutableSetOf()
        _userCategoryData.value = listOf()
    }
}