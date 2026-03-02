import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
}

setNamespace("core.designsystem")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}