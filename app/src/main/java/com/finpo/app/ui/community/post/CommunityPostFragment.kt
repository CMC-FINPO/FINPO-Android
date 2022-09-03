package com.finpo.app.ui.community.post

import android.net.Uri
import android.os.Bundle
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
import javax.inject.Inject

@AndroidEntryPoint
class CommunityPostFragment : BaseFragment<FragmentCommunityPostBinding>(R.layout.fragment_community_post) {
    private val viewModel by viewModels<CommunityPostViewModel>()
    private val args by navArgs<CommunityPostFragmentArgs>()

    @Inject lateinit var permissionManager: PermissionManager

    private var selectedUriList: List<Uri> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.id = args.id
        viewModel.editTextInput.value = args.content
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //TODO 이미지 불러오고, 해당 이미지 selectedUriList에 넣기

        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }

        viewModel.showPreparationToastEvent.observe {
            permissionManager.createGetImagePermission { showImagePicker() }
        }

        viewModel.goToCommunityHomeFragmentEvent.observe { isPostNewArticle ->
            if(isPostNewArticle)
                findNavController().navigate(CommunityPostFragmentDirections.actionCommunityPostFragmentToCommunityFragment())
            else
                findNavController().navigate(CommunityPostFragmentDirections.actionCommunityPostFragmentToCommunityDetailFragment(viewModel.id))
        }

        viewModel.finishButtonClickEvent.observe {
            val bitmapList = ImageUtils().uriListToBitmapList(requireActivity(), selectedUriList)
            viewModel.postOrPutWriting(bitmapList)
        }
    }

    private fun showImagePicker() {
        TedBottomPicker.with(requireActivity())
            .setPreviewMaxCount(Int.MAX_VALUE)
            .setCompleteButtonText("선택 완료")
            .setSelectedUriList(selectedUriList)
            .setSelectMaxCount(COMMUNITY_IMAGE_MAX_COUNT)
            .setSelectedForeground(R.drawable.ic_bg_selected_img)
            .setSelectMaxCountErrorText("최대 5장까지 첨부할 수 있습니다.")
            .showTitle(false)
            .showMultiImage { uris ->
                selectedUriList = uris
            }
    }
}