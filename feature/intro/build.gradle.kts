import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
}

setNamespace("feature.intro")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}