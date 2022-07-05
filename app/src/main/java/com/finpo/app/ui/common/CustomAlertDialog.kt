package com.finpo.app.ui.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.Window
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.finpo.app.R
import com.finpo.app.databinding.DialogAlertBinding

class CustomAlertDialog(private val context: Context) {

    private val builder: AlertDialog.Builder by lazy {
        AlertDialog.Builder(context).setView(binding.root)
    }

    private val binding: DialogAlertBinding by lazy {
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_alert, null, false)
    }

    private var dialog: AlertDialog? = null

    fun setMessage(@StringRes messageId: Int): CustomAlertDialog {
        binding.tvMessage.text = context.getText(messageId)
        return this
    }

    fun setMessage(message: CharSequence): CustomAlertDialog {
        binding.tvMessage.text = message
        return this
    }

    fun setPositiveButton(@StringRes textId: Int, listener: (view: View) -> (Unit)): CustomAlertDialog {
        binding.btnPositive.apply {
            text = context.getText(textId)
            setOnClickListener(listener)
        }
        return this
    }

    fun setPositiveButton(text: CharSequence, listener: (view: View) -> (Unit)): CustomAlertDialog {
        binding.btnPositive.apply {
            this.text = text
            setOnClickListener(listener)
        }
        return this
    }

    fun setNegativeButton(@StringRes textId: Int, listener: (view: View) -> (Unit)): CustomAlertDialog {
        binding.btnNegative.apply {
            text = context.getText(textId)
            this.text = text
            setOnClickListener(listener)
        }
        return this
    }

    fun setNegativeButton(text: CharSequence, listener: (view: View) -> (Unit)): CustomAlertDialog {
        binding.btnNegative.apply {
            this.text = text
            setOnClickListener(listener)
        }
        return this
    }

    fun hideNegativeButton(): CustomAlertDialog {
        binding.btnNegative.visibility = View.GONE
        binding.viewDivider2.visibility = View.GONE

        return this
    }

    fun create() {
        dialog = builder.create()
    }

    fun show() {
        dialog = builder.create()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}