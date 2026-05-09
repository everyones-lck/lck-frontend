import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.feature)
}

setNamespace("feature.mypage")

kotlin {
    sourceSets {
        commonMain.dependencies {

        }

        val androidMain by getting {
            dependencies {
                implementation(libs.play.services.oss.licenses)
            }
        }
    }
}