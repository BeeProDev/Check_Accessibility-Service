package com.example.accessibility

import android.content.Context
import android.provider.Settings
import android.text.TextUtils.SimpleStringSplitter
import android.util.Log

// To check if service is enabled
private fun isAccessibilitySettingsOn(context: Context): Boolean {
    var accessibilityEnabled = 0
    val service: String = context.packageName.toString() + "/" + YourAccessibilityService::class.java.getCanonicalName()
    try {
        accessibilityEnabled = Settings.Secure.getInt(
            context.applicationContext.contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED
        )
        Log.v("SERVICE_CHECK", "accessibilityEnabled = $accessibilityEnabled")
    } catch (e: Settings.SettingNotFoundException) {
        Log.e(
            "SERVICE_CHECK", "Error finding setting, default accessibility to not found: ${e.message}"
        )
    }
    val mStringColonSplitter = SimpleStringSplitter(':')
    if (accessibilityEnabled == 1) {
        val settingValue: String = Settings.Secure.getString(
            context.applicationContext.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        mStringColonSplitter.setString(settingValue)
        while (mStringColonSplitter.hasNext()) {
            val accessibilityService = mStringColonSplitter.next()
            Log.v("SERVICE_CHECK", "accessibilityService : $accessibilityService $service")
            if (accessibilityService.equals(service, ignoreCase = true)) {
                Log.v("SERVICE_CHECK", "We've found the correct setting - accessibility is switched on!")
                return true
            }
        }
    } else {
        Log.v("SERVICE_CHECK", "SERVICE IS DISABLED")
    }
    return false
}