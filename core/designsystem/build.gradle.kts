import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
    alias(libs.plugins.everylol.compose)
}

setNamespace("core.designsystem")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}