import every.lol.com.setNamespace

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.everylol.library)
}

setNamespace("core.data")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:network"))
           // implementation(project(":core:module"))
            implementation(project(":core:datastore"))
            api(project(":core:domain"))

            implementation(libs.koin.core)
        }
    }
}