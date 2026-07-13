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
            implementation(project(":core:model"))
            implementation(project(":core:datastore"))
            implementation(project(":core:common"))
            api(project(":core:domain"))
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
            implementation(libs.koin.core)
        }
    }
}