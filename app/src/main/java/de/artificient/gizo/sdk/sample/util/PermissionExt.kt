package de.artificient.gizo.sdk.sample.util

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat

fun Context.hasPermission(vararg permissions: String): Boolean {
    val result = mutableListOf<Boolean>()
    val sdkVersion29OrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    val sdkVersion31OrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val sdkVersion33OrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    permissions.forEach forEach@{
        if (sdkVersion29OrAbove && it == Manifest.permission.READ_EXTERNAL_STORAGE){
            return@forEach
        }
        if (sdkVersion29OrAbove && it == Manifest.permission.WRITE_EXTERNAL_STORAGE){
            return@forEach
        }
        if (sdkVersion33OrAbove.not() && it == Manifest.permission.POST_NOTIFICATIONS){
            return@forEach
        }
        if (sdkVersion31OrAbove.not() && it ==  Manifest.permission.HIGH_SAMPLING_RATE_SENSORS){
            return@forEach
        }
        val isGranted = ContextCompat.checkSelfPermission(
            this,
            it
        ) == PackageManager.PERMISSION_GRANTED
        result.add(isGranted)
    }
    val predicate: (Boolean) -> Boolean = { !it }
    return !result.any(predicate)
}

fun  Context.hasUsageStatsPermission(): Boolean {
    val appOps = getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
    val mode = appOps.unsafeCheckOpNoThrow(android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(), packageName)
    return mode == android.app.AppOpsManager.MODE_ALLOWED
}

fun  Context.hasAlarmPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }
}

fun Context.isIgnoringBatteryOptimizations(): Boolean {
    val pm: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
    return pm.isIgnoringBatteryOptimizations(packageName)
}

fun checkInitialPermissions(context: Context): Boolean {

    context.hasPermission(
        *arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.HIGH_SAMPLING_RATE_SENSORS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
        )
    ).also { hasPermission ->
        if(hasPermission.not()) {
            return false
        }
    }

    if (context.hasAlarmPermission().not() ||
        context.isIgnoringBatteryOptimizations().not() ||
        context.hasUsageStatsPermission().not()
    ){
        return false
    }

    return true
}