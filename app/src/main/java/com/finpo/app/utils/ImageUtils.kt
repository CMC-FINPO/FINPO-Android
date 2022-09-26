package com.finpo.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.finpo.app.model.remote.ImageOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


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

    fun getMultipartBodyImgFromBitmap(bitmap: Bitmap?, name: String, filename: String): MultipartBody.Part? {
        val profileImage = bitmap?.let { BitmapRequestBody(it) }
        return if (profileImage == null) null
        else MultipartBody.Part.createFormData(
            name,
            filename,
            profileImage
        )
    }

    fun getMultipartBodyImgListFromBitmapList(bitmapList: List<Bitmap?>): List<MultipartBody.Part?> {
        val tempMultipartBodyImgList = mutableListOf<MultipartBody.Part?>()
        bitmapList.forEach { bitmap -> tempMultipartBodyImgList.add(getMultipartBodyImgFromBitmap(bitmap, "imgFiles", "imagefile.jpeg")) }
        return tempMultipartBodyImgList
    }

    suspend fun uriToBitmap(context: Context, imageUri: Uri): Bitmap? = withContext(IO) {
        kotlin.runCatching {
            Glide
                .with(context)
                .asBitmap()
                .load(imageUri)
                .submit()
                .get()
        }.getOrNull()
    }

//    suspend fun uriToBitmap(context: Context, imageUri: Uri): Bitmap? = suspendCoroutine { emitter ->
//        Glide.with(context)
//            .asBitmap().load(imageUri)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .skipMemoryCache(true)
//            .listener(object : RequestListener<Bitmap?> {
//                override fun onLoadFailed(
//                    e: GlideException?, model: Any?, target: Target<Bitmap?>?, isFirstResource: Boolean
//                ): Boolean {
//                    emitter.resume(null)
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Bitmap?, model: Any?,
//                    target: Target<Bitmap?>?, dataSource: DataSource?, isFirstResource: Boolean
//                ): Boolean {
//                    emitter.resume(resource)
//                    return true
//                }
//            }
//            ).submit()
//    }

    suspend fun uriListToBitmapList(context: Context, imageUriList: List<Uri>): List<Bitmap?> {
        return try {
            val tempBitmapList = mutableListOf<Bitmap?>()
            imageUriList.forEach { uri ->
                tempBitmapList.add(uriToBitmap(context, uri))
            }
            tempBitmapList
        } catch (e: Exception) {
            listOf()
        }
    }

    fun imageOrderListToUriList(imageOrderList: List<ImageOrder>): List<Uri> {
        val tmpList = mutableListOf<Uri>()
        try {
            imageOrderList.forEach { imageOrder ->
                val url = URL(imageOrder.img)
                val uri = Uri.parse(url.toURI().toString())
                tmpList.add(uri)
            }
        } catch (e: Exception) {}
        return tmpList
    }
}