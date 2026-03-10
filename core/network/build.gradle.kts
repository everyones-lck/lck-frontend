import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import every.lol.com.setNamespace

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.everylol.library)
    alias(libs.plugins.buildconfig)
}

setNamespace("core.network")


fun getProperty(key: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(key)?:""
}

buildConfig {
    packageName("every.lol.com.core.network")
    //useKotlinOutput { topLevelConstants = true }
    buildConfigField("String", "BASE_URL", "\"${getProperty("BASE_URL")}\"")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
        }
    }
}