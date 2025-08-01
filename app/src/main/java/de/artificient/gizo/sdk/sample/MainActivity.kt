package de.artificient.gizo.sdk.sample

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dagger.hilt.android.AndroidEntryPoint
import de.artificient.gizo.sdk.model.CrashMode
import de.artificient.gizo.sdk.sample.designsystem.component.RowSwitchItem
import de.artificient.gizo.sdk.sample.recording.presentation.camera.RecordingCameraActivity
import de.artificient.gizo.sdk.sample.recording.presentation.nocamera.RecordingNoCameraActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var assistedFactory: MainActivityViewModel.ViewModelAssistedFactory
    private val viewModel: MainActivityViewModel by lazy {
        ViewModelProvider(
            applicationContext as ViewModelStoreOwner, MainActivityViewModel.Factory(
                assistedFactory
            )
        )[MainActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val uiState by viewModel.uiState.collectAsState()

            Screen(
                uiState = uiState,
                onDriveNow = {
                    startDriveNow(context)
                },
                onDriveNowWithoutCamera = {
                    startDriveNowWithoutCamera(context)
                },
                onCrashDetectionChange = {
                    viewModel.crashDetectionChange(it)
                },
                onCrashModeChange = {
                    viewModel.accidentTestModeChange(it)
                }
            )
        }
    }

    private fun startDriveNow(context: Context) {
        startActivity(Intent(context, RecordingCameraActivity::class.java))
    }

    private fun startDriveNowWithoutCamera(context: Context) {
        startActivity(Intent(context, RecordingNoCameraActivity::class.java))
    }

    @Preview
    @Composable
    fun Screen(
        onDriveNow: () -> Unit = {},
        onDriveNowWithoutCamera: () -> Unit = {},
        onCrashDetectionChange: (Boolean) -> Unit = {},
        onCrashModeChange: (CrashMode) -> Unit = {},
        uiState: MainActivityUiState = MainActivityUiState(),
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { onDriveNowWithoutCamera() },
                    modifier = Modifier,
                    enabled = true,
                    shape = RoundedCornerShape(16.dp),
                    content = {
                        Text(
                            text = "Drive Now Without Camera",
                            style = MaterialTheme.typography.button.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            ),
                            color = Color.White,
                        )

                    }
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text("Device ID: ${getDeviceID(context = this@MainActivity)}")

                Spacer(modifier = Modifier.height(40.dp))

                CrashDetection(modifier = Modifier, uiState = uiState){
                    onCrashDetectionChange(it)
                }


                Spacer(modifier = Modifier.height(40.dp))

                CrashModeSelector(
                    uiState = uiState,
                    onModeSelected = {
                        onCrashModeChange(it)
                    }
                )
            }
        }
    }

    @Composable
    fun CrashModeSelector(
        uiState: MainActivityUiState = MainActivityUiState(),
        onModeSelected: (CrashMode) -> Unit
    ) {
        val options = CrashMode.values()

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .background(Color(0xFF1C1C1E), RoundedCornerShape(12.dp))
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEach { mode ->
                val isSelected = uiState.accidentTestMode == mode

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color(0xFFB8B8C0) else Color.Transparent)
                        .clickable { onModeSelected(mode) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mode.name.replace("_", " ").lowercase().replaceFirstChar(Char::uppercase),
                        color = if (isSelected) Color.Black else Color.White,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    fun CrashDetection(modifier: Modifier = Modifier, uiState: MainActivityUiState = MainActivityUiState(), onSwitchChange: (Boolean) -> Unit = {}) {

        RowSwitchItem(
            text = "Crash Detection",
            switchOn = uiState.crashDetection,
            onSwitchChange = {
                onSwitchChange(it)
            }
        )
    }

    @Preview
    @Composable
    fun AccidentMode(modifier: Modifier = Modifier, uiState: MainActivityUiState = MainActivityUiState(), onSwitchChange: (Boolean) -> Unit = {}) {

        CrashModeSelector(
            uiState = uiState,
            onModeSelected = {
            }
        )
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceID(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}