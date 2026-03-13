import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
    alias(libs.plugins.everylol.compose)
}

setNamespace("core.common")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.moko.permissions.compose)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
        }
        iosMain.dependencies {

        }
    }
}