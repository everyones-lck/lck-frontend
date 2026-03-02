import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
}

setNamespace("core.data")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}