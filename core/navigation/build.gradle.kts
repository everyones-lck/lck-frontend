import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
    alias(libs.plugins.everylol.compose)
    alias(libs.plugins.kotlin.serialization)
}

setNamespace("core.navigation")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
    }
}