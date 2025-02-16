package de.artificient.gizo.sdk.sample.recording.presentation.camera

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import de.artificient.gizo.sdk.sample.designsystem.theme.AppTheme
import de.artificient.gizo.sdk.sample.recording.presentation.RecordingViewModel
//import de.artificient.gizo.sdk.sample.recording.presentation.service.RecordingCameraService
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class RecordingCameraActivity : ComponentActivity() {
    @Inject
    lateinit var assistedFactory: RecordingViewModel.ViewModelAssistedFactory
    private val viewModel: RecordingViewModel by lazy {
        ViewModelProvider(
            applicationContext as ViewModelStoreOwner, RecordingViewModel.Factory(
                assistedFactory
            )
        )[RecordingViewModel::class.java]
    }

    private var previewView: PreviewView?=null

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    RecordingScreen(viewModel = viewModel,
                        onAttachPreview = { preview ->
                            previewView=preview
                            attachCamera()
                        },
                        onClose = { finishAction() })
                }
            }
        }
    }

    private fun finishAction() {
        setResult(Activity.RESULT_OK)
        super.finish()
    }

    private fun attachCamera() {
        previewView?.let{
            viewModel.attachCamera(this, it)
        }
    }

    public override fun onStart() {
        super.onStart()
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
    public override fun onStop() {
        window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onStop()
    }

}