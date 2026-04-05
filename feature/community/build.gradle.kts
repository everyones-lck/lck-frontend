import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.feature)
}

setNamespace("feature.community")

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation("androidx.media3:media3-exoplayer:1.3.1")
            implementation("androidx.media3:media3-ui:1.3.1")
            implementation("androidx.media3:media3-common:1.3.1")
        }

        commonMain.dependencies {

        }
    }
}