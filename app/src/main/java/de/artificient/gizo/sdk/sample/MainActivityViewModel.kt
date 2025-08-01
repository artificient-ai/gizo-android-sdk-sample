package de.artificient.gizo.sdk.sample

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import de.artificient.gizo.sdk.Gizo
import de.artificient.gizo.sdk.model.CrashMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivityViewModel @AssistedInject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    val gizoAnalysis = (context as Application).gizoAnalysis

    companion object {
        val TAG = MainActivityViewModel::class.simpleName
    }

    init{
        gizoAnalysis.didStartRecording = {
            var eee = 0
        }

        gizoAnalysis.didStopRecording = {
            var eee = 0
        }

        gizoAnalysis.onDetectedCrash = { crashDetected ->
            var e = crashDetected.time
        }

        gizoAnalysis.onUploadedCrash = { crashUploaded ->
            var e = crashUploaded.time
        }
    }

    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState = _uiState.asStateFlow()

    fun crashDetectionChange(crashDetectionStatus: Boolean){
        viewModelScope.launch {
            if (crashDetectionStatus) {
                Gizo.app.setup { option ->
                    option.toBuilder()
                        .crashSetting(
                            option.crashSetting.toBuilder()
                                .detectCrash(true)
                                .build()
                        )
                        .build()
                }
                _uiState.update { it.copy(crashDetection = true) }
            } else {
                Gizo.app.setup { option ->
                    option.toBuilder()
                        .crashSetting(
                            option.crashSetting.toBuilder()
                                .detectCrash(false)
                                .build()
                        )
                        .build()
                }
                _uiState.update { it.copy(crashDetection = false) }

            }
        }
    }

    fun accidentTestModeChange(accidentTestModeStatus: Boolean){
        viewModelScope.launch {
            if (accidentTestModeStatus) {
                Gizo.app.setup { option ->
                    option.toBuilder()
                        .crashSetting(
                            option.crashSetting.toBuilder()
                                .mode(CrashMode.STILL_TEST)
                                .build()
                        )
                        .build()
                }
                _uiState.update { it.copy(accidentTestMode = true) }
            } else {
                Gizo.app.setup { option ->
                    option.toBuilder()
                        .crashSetting(
                            option.crashSetting.toBuilder()
                                .mode(CrashMode.PROD)
                                .build()
                        )
                        .build()
                }
                _uiState.update { it.copy(accidentTestMode = false) }

            }
        }
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
        fun create(): MainActivityViewModel
    }

}
