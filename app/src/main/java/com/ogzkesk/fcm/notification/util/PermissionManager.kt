package com.ogzkesk.fcm.notification.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

internal object PermissionManager {

    fun checkNotificationPermission(context: Context){
        if(!hasNotificationPermission(context)){
            askNotificationPermission(context)
        }
    }

    fun hasNotificationPermission(context: Context): Boolean {
        if (!checkApiGreaterThan32()) {
            return true
        }
        return ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun askNotificationPermission(context: Context) {
        if (!checkApiGreaterThan32()) {
            return
        }
        ActivityCompat.requestPermissions(
            context as Activity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0
        )
    }

    private fun checkApiGreaterThan32(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }
}