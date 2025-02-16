package de.artificient.gizo.sdk.sample

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import dagger.hilt.android.HiltAndroidApp
import de.artificient.gizo.sdk.Gizo
import de.artificient.gizo.sdk.setting.GizoOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class Application : Application(), ViewModelStoreOwner {

    override fun onCreate() {
        super.onCreate()
        val result = Gizo.initialize(
            this,
            GizoOptions.Builder(license = "YOUR_LICENSE")
                .clientId("YOUR_CLIENT_ID")
                .clientSecret("YOUR_CLIENT_SECRET")
                .build()
        )

        if(result.isSuccessful){
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