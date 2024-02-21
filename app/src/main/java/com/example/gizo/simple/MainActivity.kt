package com.example.gizo.simple

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import de.artificient.gizo.sdk.Gizo
import de.artificient.gizo.sdk.GizoAnalysis


class MainActivity : ComponentActivity() {
    private val recordingPermission: Array<String>
        get() {
            //Other permissions can be added here
            return Gizo.app.permissionRequired
        }

    private val gizoAnalysis: GizoAnalysis = Gizo.app.gizoAnalysis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val isPermissionGranted = remember { mutableStateOf(false) }

            val previewView: PreviewView = remember { PreviewView(context) }

            gizoAnalysis.start(lifecycleOwner = this) {
            }

            gizoAnalysis.startCamera(lifecycleOwner = this) {
                attachPreview(previewView = previewView)
            }

            val launcherDrivePermission = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissionsMap ->
                val areGranted = permissionsMap.mapNotNull {
                    it.value
                }.reduce { acc, next -> acc && next }

                if (areGranted) {
                    isPermissionGranted.value = true
                }
            }

            val usageAccessSettingsIntent = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { activityResult ->
                if (activityResult.resultCode == RESULT_OK)
                    isPermissionGranted.value = true
            }

            LaunchedEffect(true) {
                checkAndRequestLocationPermissions(
                    context,
                    recordingPermission,
                    launcherDrivePermission
                ) {
                    if (recordingPermission.contains(Manifest.permission.READ_PHONE_STATE)) {
                        if (hasUsageStatsPermission()) {
                            isPermissionGranted.value = true
                        } else {
                            usageAccessSettingsIntent.launch(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                        }
                    } else {
                        isPermissionGranted.value = true
                    }
                }
            }

            if (isPermissionGranted.value) {
                Screen(gizoAnalysis = gizoAnalysis, previewView = previewView)

            }
        }
    }

    private fun attachPreview(previewView: PreviewView) {
        previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        gizoAnalysis.attachPreview(previewView)
    }

    override fun onDestroy() {
        super.onDestroy()
        gizoAnalysis.stopCamera()
        gizoAnalysis.stop()
    }
}

private fun checkAndRequestLocationPermissions(
    context: Context,
    permission: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    hasPermissionCallback: () -> Unit
) {

    if (permission.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    ) {
        hasPermissionCallback()
        // Use location because permissions are already granted
    } else {
        // Request permissions
        launcher.launch(permission)
    }
}

@Composable
fun Screen(
    gizoAnalysis: GizoAnalysis,
    previewView: PreviewView
) {

    val recordingFlag = remember { mutableStateOf(false) }
    val previewFlag = remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    if (previewFlag.value) {
        previewView.let { preview ->
            AndroidView(
                factory = { preview },
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
        }
    }

    ScreenDetails(
        recordingFlag = recordingFlag,
        previewFlag = previewFlag,
        onRecordingClick = {
            if (recordingFlag.value) {
                coroutineScope.launch {
                    gizoAnalysis.startSavingSession()
                }
            } else {
                gizoAnalysis.stopSavingSession()
            }
            recordingFlag.value = !recordingFlag.value
        },
        onPreviewClick = {
            if (previewFlag.value) {
                gizoAnalysis.lockPreview()
            } else {
                gizoAnalysis.unlockPreview(previewView)
            }
            previewFlag.value = !previewFlag.value
        }
    )
}

@Composable
fun ScreenDetails(
    recordingFlag: MutableState<Boolean>,
    previewFlag: MutableState<Boolean>,
    onRecordingClick: () -> Unit,
    onPreviewClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {

        Row(
            modifier = Modifier
                .padding(top = 12.dp, start = 12.dp)
                .align(Alignment.TopStart),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = if (recordingFlag.value) R.drawable.gizo_ic_stop else R.drawable.gizo_ic_record),
                contentDescription = "record",
                modifier = Modifier
                    .size(64.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onRecordingClick()
                    },
            )
            Spacer(modifier = Modifier.size(12.dp))
            Image(
                painter = painterResource(id = if (previewFlag.value) R.drawable.gizo_ic_preview_hide else R.drawable.gizo_ic_preview_show),
                contentDescription = "toggle preview",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onPreviewClick()
                    },
            )
        }
    }
}


@Preview(device = Devices.DEFAULT, widthDp = 720, heightDp = 320)
@Composable
fun ScreenPreview() {
    ScreenDetails(
        recordingFlag = remember { mutableStateOf(true) },
        previewFlag = remember { mutableStateOf(true) },
        onRecordingClick = {},
        onPreviewClick = {}
    )
}

fun Context.hasUsageStatsPermission(): Boolean {
    val appOps = getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val mode = appOps.unsafeCheckOpNoThrow(
            android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(), packageName
        )
        mode == android.app.AppOpsManager.MODE_ALLOWED
    } else {
        true
    }
}