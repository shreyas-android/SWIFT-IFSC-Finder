plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.jackson.android.bank.detail"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jackson.android.bank.detail"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val keystoreFile = project.rootProject.file("cred.properties")
        val credProperties = org.jetbrains.kotlin.konan.properties.Properties()
        credProperties.load(keystoreFile.inputStream())

        buildConfigField(
            "String", "SWIFT_API_KEY", credProperties.getProperty("SWIFT_API_KEY"))

    }



    val keystoreFile = project.rootProject.file("gradle.properties")
    val properties = org.jetbrains.kotlin.konan.properties.Properties()
    properties.load(keystoreFile.inputStream())

    signingConfigs {
        create("jackson") {
            storeFile = file(properties.getProperty("JACKSON_RELEASE_STORE_FILE"))
            storePassword = properties.getProperty("JACKSON_RELEASE_STORE_PASSWORD")
            keyAlias = properties.getProperty("JACKSON_RELEASE_KEY_ALIAS")
            keyPassword = properties.getProperty("JACKSON_RELEASE_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig =  signingConfigs.getByName("jackson")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures{
        buildConfig = true
        compose = true

    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":domain:bankdetail"))
    implementation(project(":common:shared"))

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.activity)
    implementation(libs.compose.ui.ui)
    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.constraint.layout)
    implementation(libs.compose.material.material)
    implementation(libs.compose.viewmodel)
    implementation(libs.compose.material3)
    implementation(libs.compose.navigation)

    implementation("com.github.shreyas-android:SKMPUIThemeLibrary:1.0.0")
}