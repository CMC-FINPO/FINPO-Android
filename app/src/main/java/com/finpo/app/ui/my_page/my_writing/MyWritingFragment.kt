package com.finpo.app.ui.my_page.my_writing

import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentMyWritingBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.my_page.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyWritingFragment : BaseFragment<FragmentMyWritingBinding>(R.layout.fragment_my_writing) {
    private val viewModel by viewModels<MyPageViewModel>({requireParentFragment()})

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        if(viewModel.isInitDataCompleted) viewModel.myWritingLiveData.changeMyWriting()
    }
}