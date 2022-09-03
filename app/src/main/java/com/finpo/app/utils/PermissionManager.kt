package com.finpo.app.utils

import android.Manifest
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import javax.inject.Inject

class PermissionManager @Inject constructor() {
    fun setPermissionListener(doWhenPermissionGranted : () -> Unit): PermissionListener {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                doWhenPermissionGranted()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {}
        }
        return permissionListener
    }

    fun createGetImagePermission(permissionListener: PermissionListener) {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }
}