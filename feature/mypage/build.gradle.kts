import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
}

setNamespace("feature.mypage")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}