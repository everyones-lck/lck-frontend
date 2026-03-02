import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
}

setNamespace("core.navigation")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}