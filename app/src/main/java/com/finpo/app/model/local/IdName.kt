package com.finpo.app.model.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class IdName(
    var id: Int?,
    var name: String
) : Parcelable