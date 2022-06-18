package com.finpo.app.utils

import android.R.attr.bitmap
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody


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

    fun uriToBitmap(context: Context, imageUri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        context.contentResolver,
                        imageUri
                    )
                )
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            }
        } catch (e: Exception) {
            null
        }
    }
}