package com.finpo.app.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

fun String?.isJsonObject(): Boolean = this?.startsWith("{") == true && this.endsWith("}")
fun String?.isJsonArray(): Boolean = this?.startsWith("[") == true && this.endsWith("]")
fun String?.toPlainRequestBody() = requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull())