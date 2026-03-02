import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
}

setNamespace("feature.main")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}