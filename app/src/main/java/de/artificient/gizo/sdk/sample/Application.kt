package de.artificient.gizo.sdk.sample

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import dagger.hilt.android.HiltAndroidApp
import de.artificient.gizo.sdk.Gizo
import de.artificient.gizo.sdk.GizoAnalysis
import de.artificient.gizo.sdk.setting.GizoCrashSetting
import de.artificient.gizo.sdk.setting.GizoOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class Application : Application(), ViewModelStoreOwner {

    private lateinit var _gizoAnalysis: GizoAnalysis
    val gizoAnalysis: GizoAnalysis
        get() = _gizoAnalysis

    fun setGizoAnalysis(analysis: GizoAnalysis) {
        _gizoAnalysis = analysis
    }

    override fun onCreate() {
        super.onCreate()
        val result = Gizo.initialize(
            this,
            GizoOptions.Builder(license = "YOUR_LICENSE")
                .clientId("YOUR_CLIENT_ID")
                .clientSecret("YOUR_CLIENT_SECRET")
                .crashSetting(
                    GizoCrashSetting.Builder()
                        .detectCrash(true)
                        .logToFile(true)
                        .build()
                )
                .build()
        )

        if(result.isSuccessful){
            setGizoAnalysis(Gizo.app.gizoAnalysis)
            Gizo.enableDetections()
            Gizo.setToken("YOUR_CLIENT_ID", "YOUR_CLIENT_SECRET")
            CoroutineScope(Dispatchers.IO).launch {

                try {
                    val status = Gizo.setUserId(1) // your user id
//                    val userId = Gizo.createUser()
                } catch (e: Exception) {
                }
            }
        } else {
        }
    }

    private val appViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }
    override val viewModelStore: ViewModelStore
        get() = appViewModelStore
}