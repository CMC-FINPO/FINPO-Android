package com.finpo.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class GlideUtils(val context: Context) {
    suspend fun imageUrlToBitmap(imageUrl: String?): Bitmap? = withContext(IO) {
        kotlin.runCatching {
            Glide
                .with(context)
                .asBitmap()
                .load(imageUrl)
                .submit()
                .get()
        }.getOrNull()
    }
}