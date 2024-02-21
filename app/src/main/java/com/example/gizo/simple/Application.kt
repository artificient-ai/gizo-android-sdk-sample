package com.example.gizo.simple

import android.app.Application
import android.util.Log
import androidx.camera.video.Quality
import de.artificient.gizo.sdk.Gizo
import de.artificient.gizo.sdk.model.AnalysisDelegateType
import de.artificient.gizo.sdk.model.FileLocationPath
import de.artificient.gizo.sdk.setting.*

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        Gizo.initialize(
            this,
            GizoAppOptions.Builder("GIZO_LICENSE")
                .debug(true)
                .folderName("GizoSample")
                .analysisSetting(GizoAnalysisSettings.Builder()
                    .allowAnalysis(true)
                    .modelName("arti_sense.data")
                    .loadDelegate(AnalysisDelegateType.Auto)
                    .collisionThreshold(0.5f)
                    .tailgatingThreshold(1.0f)
                    .saveTtcCsvFile(true)
                    .ttcFileLocation(FileLocationPath.CACHE)
                    .saveDataTimerPeriod(10L)
                    .saveMatrixFile(true)
                    .matrixFileLocation(FileLocationPath.CACHE)
                    .build()
                )
                .gpsSetting(
                    GizoGpsSetting.Builder()
                        .allowGps(true)
                        .mapBoxKey("MAPBOX_PUBLIC_KEY")
                        .interval(1000L)
                        .maxWaitTime(1000L)
                        .withForegroundService(false)
                        .saveCsvFile(true)
                        .fileLocation(FileLocationPath.CACHE)
                        .saveDataTimerPeriod(10L)
                        .build()
                )
                .imuSetting(
                    GizoImuSetting.Builder()
                        .allowAccelerationSensor(true)
                        .allowMagneticSensor(true)
                        .allowGyroscopeSensor(true)
                        .saveCsvFile(true)
                        .fileLocation(FileLocationPath.CACHE)
                        .saveDataTimerPeriod(10L)
                        .build()
                )
                .videoSetting(
                    GizoVideoSetting.Builder()
                        .allowRecording(true)
                        .quality(Quality.HD)
                        .fileLocation(FileLocationPath.CACHE)
                        .build()
                )
                .batterySetting(
                    GizoBatterySetting.Builder()
                        .checkBattery(true)
                        .saveCsvFile(true)
                        .checkTemperature(true)
                        .lowBatteryLimit(25f)
                        .lowBatteryStop(15f)
                        .interval(5000L)
                        .build()
                )
                .orientationSetting(
                    GizoOrientationSetting.Builder()
                        .allowGravitySensor(true)
                        .build()
                )
                .userActivitySetting(
                    GizoUserActivitySetting.Builder()
                        .allowUserActivity(true)
                        .saveCsvFile(true)
                        .build()
                )
                .deviceEventSetting(
                    GizoDeviceEventSetting.Builder()
                        .allowDeviceEvent(true)
                        .saveCsvFile(true)
                        .build()
                )
                .build()
        )

        Gizo.app.setLoadModelObserver { status ->
            Log.d("LoadModelStatus", "status:" + status.name)
        }

        Gizo.app.loadModel()
    }
}