import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
}

setNamespace("core.datastore")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}