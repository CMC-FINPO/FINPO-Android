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
import okhttp3.MultipartBody
import java.lang.Exception

class ImageUtils {
    suspend fun imageUrlToBitmap(context: Context, imageUrl: String?): Bitmap? = withContext(IO) {
        kotlin.runCatching {
            Glide
                .with(context)
                .asBitmap()
                .load(imageUrl)
                .submit()
                .get()
        }.getOrNull()
    }

    fun getProfileImgFromBitmap(bitmap: Bitmap?): MultipartBody.Part? {
        val profileImage = bitmap?.let { BitmapRequestBody(it) }
        return if (profileImage == null) null
        else MultipartBody.Part.createFormData(
            "profileImgFile",
            "imagefile.jpeg",
            profileImage
        )
    }
}