import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.everylol.library)
    alias(libs.plugins.buildconfig)
}

setNamespace("core.notification")


kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)

        }
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
        }
    }
}