import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.feature)
}

setNamespace("feature.main")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}