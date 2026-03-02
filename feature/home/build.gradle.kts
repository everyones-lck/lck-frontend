import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.feature)
}

setNamespace("feature.home")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}