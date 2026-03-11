import every.lol.com.setNamespace

plugins {
    alias(libs.plugins.everylol.library)
}

setNamespace("core.datastore")

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.datastore.preferences)
            api(libs.androidx.datastore.core)
        }

        commonMain.dependencies {

            implementation(libs.koin.core)
            implementation(libs.coroutines.core)
        }
    }
}