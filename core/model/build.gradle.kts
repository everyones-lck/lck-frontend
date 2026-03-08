import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
}

setNamespace("core.model")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}