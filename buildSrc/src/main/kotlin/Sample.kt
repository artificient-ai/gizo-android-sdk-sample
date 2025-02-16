import org.gradle.api.JavaVersion

object Sample {
    /*
    MAJOR version when you make incompatible API changes,
    MINOR version when you add functionality in a backwards-compatible manner, and
    PATCH version when you make backwards-compatible bug fixes.
 */
    private const val versionMajor = 0
    private const val versionMinor = 0
    private const val versionPatch = 1
    const val compileSdk = 34
    const val minSdk = 29
    const val targetSdk = 34
    const val versionCode = versionMajor * 10000 + versionMinor * 100 + versionPatch
    const val versionName = "$versionMajor.$versionMinor.$versionPatch"
    const val namespace = "de.artificient.gizo"
    const val applicationId = "de.artificient.gizo.sdk.sample"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    const val jvmTarget = "1.8"
    val jvmVersion = JavaVersion.VERSION_1_8
    const val kotlinCompilerExtensionVersion = Compose.composeVersion
}