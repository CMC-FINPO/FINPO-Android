package com.finpo.app.ui.community.post

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunityPostBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.utils.COMMUNITY_IMAGE_MAX_COUNT
import com.finpo.app.utils.ImageUtils
import com.finpo.app.utils.PermissionManager
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CommunityPostFragment : BaseFragment<FragmentCommunityPostBinding>(R.layout.fragment_community_post) {
    private val viewModel by viewModels<CommunityPostViewModel>()
    private val args by navArgs<CommunityPostFragmentArgs>()
    private lateinit var communityImagePostAdapter: CommunityImagePostAdapter

    @Inject lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.id = args.id
        viewModel.editTextInput.value = args.content?.content ?: ""

        if(args.content?.imgs != null)
            viewModel.updateUriList(ImageUtils().imageOrderListToUriList(args.content!!.imgs!!))
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        communityImagePostAdapter = CommunityImagePostAdapter(viewModel)
        binding.rvImage.adapter = communityImagePostAdapter

        viewModel.selectedUriList.observe(viewLifecycleOwner) {
            communityImagePostAdapter.submitList(it.toList())
        }

        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }

        viewModel.showPreparationToastEvent.observe {
            permissionManager.createGetImagePermission { showImagePicker() }
        }

        viewModel.goToCommunityHomeFragmentEvent.observe { isPostNewArticle ->
            hideLoadingDialog()
            if(isPostNewArticle)
                findNavController().navigate(CommunityPostFragmentDirections.actionCommunityPostFragmentToCommunityFragment())
            else
                findNavController().navigate(CommunityPostFragmentDirections.actionCommunityPostFragmentToCommunityDetailFragment(viewModel.id))
        }

        viewModel.finishButtonClickEvent.observe {
            showLoadingDialog()
            CoroutineScope(IO).launch {
                val bitmapList = ImageUtils().uriListToBitmapList(
                    requireActivity(),
                    viewModel.selectedUriList.value!!
                )
                viewModel.postOrPutWriting(bitmapList)
            }
        }
    }

    private fun showImagePicker() {
        TedBottomPicker.with(requireActivity())
            .setPreviewMaxCount(Int.MAX_VALUE)
            .setCompleteButtonText("선택 완료")
            .setSelectedUriList(viewModel.selectedUriList.value!!)
            .setSelectMaxCount(COMMUNITY_IMAGE_MAX_COUNT)
            .setSelectedForeground(R.drawable.ic_bg_selected_img)
            .setSelectMaxCountErrorText("최대 5장까지 첨부할 수 있습니다.")
            .showTitle(false)
            .showMultiImage { uris ->
                viewModel.updateUriList(uris)
            }
    }
}