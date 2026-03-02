import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.everylol.application)
}

kotlin {

    sourceSets {
        androidMain.dependencies {

        }

        commonMain.dependencies {

            implementation(projects.core.common)
            implementation(projects.core.data)
            implementation(projects.core.datastore)
            implementation(projects.core.designsystem)
            implementation(projects.core.domain)
            implementation(projects.core.model)
            implementation(projects.core.navigation)
            implementation(projects.core.network)
            implementation(projects.core.ui)

            implementation(projects.feature.aboutlck)
            implementation(projects.feature.community)
            implementation(projects.feature.home)
            implementation(projects.feature.intro)
            implementation(projects.feature.main)
            implementation(projects.feature.mypage)

        }

        commonTest.dependencies {

        }

        iosMain.dependencies {

        }
    }
}

android {
    namespace = "every.lol.com"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "every.lol.com"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

