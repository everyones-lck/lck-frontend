import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.feature)
}

setNamespace("feature.community")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}