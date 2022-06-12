package com.finpo.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide

class GlideUtils(val context: Context) {
    fun imageUrlToBitmap(imageUrl: String?): Bitmap? {
        return try {
            Glide
                .with(context)
                .asBitmap()
                .load(imageUrl)
                .submit()
                .get()
        } catch (e: Exception) {
            Log.e("Glide","$e")
            null
        }
    }
}