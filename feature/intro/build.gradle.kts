import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.feature)
}

setNamespace("feature.intro")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}