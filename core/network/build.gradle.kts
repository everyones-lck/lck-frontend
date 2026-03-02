import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
}

setNamespace("core.network")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}