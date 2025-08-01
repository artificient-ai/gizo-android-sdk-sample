package de.artificient.gizo.sdk.sample.recording.presentation

import android.content.Context
import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import de.artificient.gizo.sdk.GizoAnalysis
import de.artificient.gizo.sdk.config.GizoNoCameraSetting
import de.artificient.gizo.sdk.sample.Application
import de.artificient.gizo.sdk.setting.GizoStopRecordingSetting
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

class RecordingViewModel @AssistedInject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    companion object {
        val TAG = RecordingViewModel::class.simpleName
    }


    private val gizoAnalysis: GizoAnalysis = (context as Application).gizoAnalysis

    private val _notificationEvent = MutableSharedFlow<NotificationEvent>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val _uiState = MutableStateFlow(RecordingUiState())
    val uiState = _uiState.asStateFlow()

    private val _recordingNoCameraUiState = MutableStateFlow(RecordingNoCameraUiState())
    val noCameraUiState = _recordingNoCameraUiState.asStateFlow()

    private val _event = Channel<RecordingUiEvent>()

    private var recordingNoCameraTimerJob: Job? = null
    private var recordingBackgroundTimerJob: Job? = null

    private var tripTimer: Timer? = null

    private var isRecording = false

    init {
        gizoAnalysis.didStartRecording = {
        }

        gizoAnalysis.didStopRecording = {
            if(isRecording){
                isRecording = false
                stopRecording()
            }
        }

        gizoAnalysis.didUpdateLocation = { location ->
        }

        gizoAnalysis.onDetectedCrash = { crashDetected ->
        }

        gizoAnalysis.onUploadedCrash = { crashUploaded ->
        }
    }

    fun startRecordingVideo() {
    }

    fun attachCamera(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
    ) {
        _uiState.update {
            it.copy(isGravityAlign = false)
        }
        gizoAnalysis.attachCamera(lifecycleOwner) { preview ->
            preview.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            previewView.removeAllViews()

            previewView.addView(preview)
        }
    }

    fun startRecordingNoCamera() {
        val setting = GizoStopRecordingSetting.Builder()
            .stopRecordingOnLowBatteryLevel(10)
            .stopAiOnLowBatteryLevel(25)
            .build()

        val noCameraSetting = GizoNoCameraSetting(stopRecordingSetting = setting)

        gizoAnalysis.start(noCameraSetting)
        isRecording = true
        viewModelScope.launch {
            val startTrip = Date(System.currentTimeMillis())
            recordingNoCameraTimerJob?.cancel()
            recordingNoCameraTimerJob = viewModelScope.launch {
                tripTimer = fixedRateTimer(
                    name = "tripTimer",
                    period = 1000,
                    initialDelay = 0
                ) {
                    val dateNow = Date(System.currentTimeMillis())

                    val timeDifference = dateNow.time - startTrip.time
                    _recordingNoCameraUiState.update { state ->
                        state.copy(
                            hour = (timeDifference / 3600000).toInt(),
                            minute = ((timeDifference % 3600000) / 60000).toInt(),
                            second = ((timeDifference % 60000) / 1000).toInt(),
                            isRecording = true
                        )
                    }
                }
            }
        }
    }

    private fun stopRecording() = viewModelScope.launch {
        if(isRecording){
            isRecording = false
            gizoAnalysis.stopRecording()
        }
        recordingBackgroundTimerJob?.cancel()
        recordingNoCameraTimerJob?.cancel()
        tripTimer?.cancel()
        _recordingNoCameraUiState.update { state ->
            state.copy(
                hour = 0,
                minute = 0,
                second = 0,
                isRecording = false
            )
        }
    }

    fun endTripRecordingNoCamera() {
        stopRecording()
    }

    class Factory(
        private val assistedFactory: ViewModelAssistedFactory,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return assistedFactory.create() as T
        }
    }

    @AssistedFactory
    interface ViewModelAssistedFactory {
        fun create(): RecordingViewModel
    }
}
