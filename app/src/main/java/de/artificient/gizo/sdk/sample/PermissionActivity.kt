package de.artificient.gizo.sdk.sample

import android.Manifest
import android.app.AlarmManager
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import de.artificient.gizo.sdk.sample.util.checkInitialPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PermissionActivity : ComponentActivity() {

    private var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.CAMERA
    )

    private var REQUIRED_PERMISSIONS_LOCATION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val BACKGROUND_LOCATION_PERMISSION = Manifest.permission.ACCESS_BACKGROUND_LOCATION

    private val PERMISSION_REQUEST_CODE = 100

    private var isPermissionsGranted: MutableState<Boolean> = mutableStateOf(false)
    private var isLocationGranted: MutableState<Boolean> = mutableStateOf(false)
    private var isBackgroundLocationGranted: MutableState<Boolean> = mutableStateOf(false)
    private var isUsageAccessGranted: MutableState<Boolean> = mutableStateOf(false)
    private var isBatteryOptimizationIgnored: MutableState<Boolean> = mutableStateOf(false)
    private var allGranted: MutableState<Boolean> = mutableStateOf(false)

    private var permissionsCallback: ((Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(checkInitialPermissions(this)){
            goToMainActivity()
        }

        val sdkVersion29OrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        val sdkVersion31OrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        val sdkVersion33OrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

        if (!sdkVersion29OrAbove) {
            REQUIRED_PERMISSIONS += arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        if (sdkVersion31OrAbove) {
            REQUIRED_PERMISSIONS += arrayOf(
                Manifest.permission.HIGH_SAMPLING_RATE_SENSORS
            )
        }

        if (sdkVersion33OrAbove) {
            REQUIRED_PERMISSIONS += arrayOf(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        setContent {
            isPermissionsGranted = remember { mutableStateOf(hasPermissions(REQUIRED_PERMISSIONS)) }
            isLocationGranted = remember { mutableStateOf(hasPermissions(REQUIRED_PERMISSIONS_LOCATION)) }
            isBackgroundLocationGranted = remember { mutableStateOf(hasPermissions(arrayOf(BACKGROUND_LOCATION_PERMISSION))) }
            isUsageAccessGranted = remember { mutableStateOf(checkUsageAccessPermission())}
            isBatteryOptimizationIgnored = remember { mutableStateOf(checkBatteryOptimizationIgnored()) }
            allGranted = remember {
                mutableStateOf (
                    isPermissionsGranted.value &&
                            isLocationGranted.value &&
                            isBackgroundLocationGranted.value &&
                            isUsageAccessGranted.value &&
                            isBatteryOptimizationIgnored.value
                )
            }

            PermissionScreen(
                isPermissionsGranted = isPermissionsGranted,
                isLocationGranted = isLocationGranted,
                isBackgroundLocationGranted = isBackgroundLocationGranted,
                isUsageAccessGranted = isUsageAccessGranted,
                isBatteryOptimizationIgnored = isBatteryOptimizationIgnored,
                allGranted = allGranted,
                onRequestPermissions = {
                    requestPermissions(REQUIRED_PERMISSIONS) {
                        isPermissionsGranted.value = it
                    }
                },
                onRequestLocationPermissions = {
                    requestPermissions(REQUIRED_PERMISSIONS_LOCATION) {
                        isLocationGranted.value = it
                    }
                },
                onRequestBackgroundLocation = {
                    requestPermissions(arrayOf(BACKGROUND_LOCATION_PERMISSION)) {
                        isBackgroundLocationGranted.value = it
                    }
                },
                onRequestUsageAccess = {
                    requestUsageAccessPermission {
                        isUsageAccessGranted.value = it
                    }
                },
                onRequestIgnoreBattery = {
                    requestIgnoreBatteryOptimizationPermission {
                        isBatteryOptimizationIgnored.value = it
                    }
                },
                onProceed = {
                    goToMainActivity()
                }
            )
        }
    }


    override fun onResume() {
        super.onResume()
        // Recheck permissions dynamically when the user returns to the app.
        isUsageAccessGranted.value = checkUsageAccessPermission()
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            isBatteryOptimizationIgnored.value = checkBatteryOptimizationIgnored()
            allGranted.value =
                isPermissionsGranted.value &&
                        isLocationGranted.value &&
                        isBackgroundLocationGranted.value &&
                        isUsageAccessGranted.value &&
                        isBatteryOptimizationIgnored.value
        }
    }

    private fun checkUsageAccessPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun checkBatteryOptimizationIgnored(): Boolean {
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isIgnoringBatteryOptimizations(packageName)
    }

    private fun requestPermissions(permissions: Array<String>, onResult: (Boolean) -> Unit) {
        permissionsCallback = onResult
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            permissionsCallback?.invoke(allGranted)
            permissionsCallback = null
        }
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestUsageAccessPermission(onResult: (Boolean) -> Unit) {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )

        val granted = mode == AppOpsManager.MODE_ALLOWED
        if (!granted) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }
        onResult(granted)
    }

    private fun requestIgnoreBatteryOptimizationPermission(onResult: (Boolean) -> Unit) {
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val granted = pm.isIgnoringBatteryOptimizations(packageName)
        if (!granted) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }
        onResult(granted)
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    @Composable
    fun PermissionScreen(
        isPermissionsGranted: MutableState<Boolean>,
        isLocationGranted: MutableState<Boolean>,
        isBackgroundLocationGranted: MutableState<Boolean>,
        isUsageAccessGranted: MutableState<Boolean>,
        isBatteryOptimizationIgnored: MutableState<Boolean>,
        allGranted: State<Boolean>,
        onRequestPermissions: () -> Unit,
        onRequestLocationPermissions: () -> Unit,
        onRequestBackgroundLocation: () -> Unit,
        onRequestUsageAccess: () -> Unit,
        onRequestIgnoreBattery: () -> Unit,
        onProceed: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onRequestPermissions,
                enabled = !isPermissionsGranted.value
            ) {
                Text("Grant Required Permissions")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRequestLocationPermissions,
                enabled = isPermissionsGranted.value && !isLocationGranted.value
            ) {
                Text("Grant Location Permissions")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRequestBackgroundLocation,
                enabled = isLocationGranted.value && !isBackgroundLocationGranted.value
            ) {
                Text("Grant Background Location")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRequestUsageAccess,
                enabled = isBackgroundLocationGranted.value && !isUsageAccessGranted.value
            ) {
                Text("Enable Usage Access")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRequestIgnoreBattery,
                enabled = isUsageAccessGranted.value && !isBatteryOptimizationIgnored.value
            ) {
                Text("Ignore Battery Optimization")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onProceed,
                enabled = allGranted.value
            ) {
                Text("Go to Main Activity")
            }
        }
    }
}
