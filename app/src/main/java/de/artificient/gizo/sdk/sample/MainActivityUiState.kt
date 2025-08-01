package de.artificient.gizo.sdk.sample

import de.artificient.gizo.sdk.Gizo
import de.artificient.gizo.sdk.model.CrashMode

data class MainActivityUiState(
    val crashDetection: Boolean = Gizo.app.options.crashSetting.detectCrash,
    val accidentTestMode: Boolean = Gizo.app.options.crashSetting.mode == CrashMode.STILL_TEST,
    )