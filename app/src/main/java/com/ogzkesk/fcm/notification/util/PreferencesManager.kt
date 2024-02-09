package com.ogzkesk.fcm.notification.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


internal object PreferencesManager {

    private const val PREFERENCES_NAME = "fcm_shared_prefs"
    private const val ACCESS_TOKEN_KEY = "fcm_access_token"
    private const val DEVICE_TOKEN_KEY = "fcm_device_token"

    private var sharedPreferences: SharedPreferences? = null

    fun initSharedPreferences(context: Context){
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            PREFERENCES_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    var accessToken: String?
        get() = sharedPreferences?.getString(ACCESS_TOKEN_KEY,"")
        set(value) {
            sharedPreferences?.edit()?.putString(ACCESS_TOKEN_KEY,value)?.apply()
        }

    var deviceToken: String?
        get() = sharedPreferences?.getString(DEVICE_TOKEN_KEY,"")
        set(value) {
            sharedPreferences?.edit()?.putString(DEVICE_TOKEN_KEY,value)?.apply()
        }
}