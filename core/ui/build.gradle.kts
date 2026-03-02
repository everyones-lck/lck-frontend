import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
}

setNamespace("core.ui")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}