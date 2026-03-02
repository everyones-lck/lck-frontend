import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.feature)
}

setNamespace("feature.matches")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}