plugins {
    id(Plugins.androidApplication)
    id(Plugins.jetbrainsKotlinAndroid)
    id(Plugins.daggerHiltAndroid)
    id(Plugins.kotlinPluginKapt)
}

android {
    namespace = Sample.namespace
    compileSdk = Sample.compileSdk

    defaultConfig {
        applicationId = Sample.applicationId
        minSdk = Sample.minSdk
        targetSdk = Sample.targetSdk
        versionCode = Sample.versionCode
        versionName = Sample.versionName

        testInstrumentationRunner = Sample.testInstrumentationRunner
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Sample.kotlinCompilerExtensionVersion
    }

    compileOptions {
        sourceCompatibility =  Sample.jvmVersion
        targetCompatibility =  Sample.jvmVersion
    }
    kotlinOptions {
        jvmTarget = Sample.jvmTarget
    }

    packaging {
        jniLibs {
            pickFirsts.add("lib/x86/libc++_shared.so")
            pickFirsts.add("lib/x86_64/libc++_shared.so")
            pickFirsts.add("lib/armeabi-v7a/libc++_shared.so")
            pickFirsts.add("lib/arm64-v8a/libc++_shared.so")
        }
    }

}

dependencies {
    with(Android) {
        implementation(androidCore)
        implementation(appcompat)
        implementation(androidxTracingLifecycleService)
    }

    with(Google) {
        implementation(material)
    }

    with(Compose){
        implementation(composeBom)
        implementation(activity)
        implementation(ui)
        implementation(composeCoil)
        implementation(uiToolingPreview)
        implementation(uiTooling)
        implementation(material)
        implementation(foundation)
        implementation(material3)
    }

    with(Hilt){
        implementation(hiltAndroid)
        implementation(hiltNavigationCompose)
        kapt(hiltAndroidCompiler)
    }

    with(Accompanist) {
        implementation(systemUiController)
    }

    with(Android) {
        implementation(androidxStartup)
    }

    with(Coroutines) {
        implementation(workManager)
    }
    with(Hilt) {
        implementation(hiltWork)
        kapt(hiltWorkCompiler)
    }

    implementation("com.github.artificient-ai:gizo-android-sdk-alpha:2.0.6")
}