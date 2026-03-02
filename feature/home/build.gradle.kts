import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
}

setNamespace("feature.home")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}