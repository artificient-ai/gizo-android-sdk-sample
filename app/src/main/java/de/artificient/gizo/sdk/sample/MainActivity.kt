package de.artificient.gizo.sdk.sample

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.artificient.gizo.sdk.Gizo
import de.artificient.gizo.sdk.GizoAnalysis
import de.artificient.gizo.sdk.sample.recording.presentation.camera.RecordingCameraActivity
import de.artificient.gizo.sdk.sample.recording.presentation.nocamera.RecordingNoCameraActivity

class MainActivity : ComponentActivity() {
    private val gizoAnalysis: GizoAnalysis = Gizo.app.gizoAnalysis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gizoAnalysis.didStartRecording = {
            var eee = 0
        }

        gizoAnalysis.didStopRecording = {
            var eee = 0
        }
        setContent {
            val context = LocalContext.current

            Screen(
                onDriveNow = {
                    startDriveNow(context)
                },
                onDriveNowWithoutCamera = {
                    startDriveNowWithoutCamera(context)
                }
            )
        }
    }

    private fun startDriveNow(context: Context) {
        startActivity(Intent(context, RecordingCameraActivity::class.java))
        finish()
    }

    private fun startDriveNowWithoutCamera(context: Context) {
        startActivity(Intent(context, RecordingNoCameraActivity::class.java))
        finish()
    }

    @Preview
    @Composable
    fun Screen(onDriveNow: () -> Unit = {}, onDriveNowWithoutCamera: () -> Unit = {}) {

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
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceID(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}