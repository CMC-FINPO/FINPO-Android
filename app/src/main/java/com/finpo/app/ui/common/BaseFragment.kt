package com.finpo.app.ui.common

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.finpo.app.di.FinpoApplication
import com.finpo.app.utils.SingleLiveData

abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : Fragment() {
    lateinit var binding: B
    lateinit var builder: CustomAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doViewCreated()
    }

    abstract fun doViewCreated()

    protected fun shortShowToast(msg: String) =
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    protected fun longShowToast(msg: String) =
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

    protected infix fun <T> SingleLiveData<T>.observe(action: (T) -> Unit) {
        observe(viewLifecycleOwner, action)
    }

    fun showLoadingDialog() {
        FinpoApplication.instance.showLoadingDialog(requireActivity())
    }

    fun hideLoadingDialog() {
        FinpoApplication.instance.hideLoadingDialog()
    }

    fun showAlertDialog(message: String, positiveText: String = "확인", negativeText: String = "취소", positiveClick: () -> Unit) {
        builder = CustomAlertDialog(requireActivity())
            .setMessage(message)
            .setPositiveButton(positiveText) {
                positiveClick()
                builder.dismiss()
            }
            .setNegativeButton(negativeText) {
                builder.dismiss()
            }
        builder.show()
    }

    fun showConfirmDialog(message: String, positiveText: String = "확인") {
        builder = CustomAlertDialog(requireActivity())
            .setMessage(message)
            .setPositiveButton(positiveText) {
                builder.dismiss()
            }
            .hideNegativeButton()
        builder.show()
    }
}