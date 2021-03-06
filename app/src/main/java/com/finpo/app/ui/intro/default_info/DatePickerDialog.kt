package com.finpo.app.ui.intro.default_info

import android.app.DatePickerDialog
import android.content.Context
import com.finpo.app.ui.intro.IntroViewModel
import java.util.*

class DatePickerDialog {
    private val c: Calendar = Calendar.getInstance()
    var mYear: Int = c.get(Calendar.YEAR) - 20
    var mMonth: Int = c.get(Calendar.MONTH)
    var mDay: Int = c.get(Calendar.DAY_OF_MONTH)

    fun showDatePickerDialog(context: Context, viewModel: IntroViewModel) {

        val datePickerDialog = DatePickerDialog(context,
            { _, year, month, dayOfMonth ->
                mYear = year
                mMonth = month
                mDay = dayOfMonth
                val monthSel = String.format("%02d", month + 1)
                val daySel = String.format("%02d", dayOfMonth)
                viewModel.defaultInfoLiveData.setBirth(
                    "$year-$monthSel-$daySel"
                )
            },
            mYear,
            mMonth,
            mDay
        )

        datePickerDialog.show()
    }
}