import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.feature)
}

setNamespace("feature.mypage")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }
    }
}