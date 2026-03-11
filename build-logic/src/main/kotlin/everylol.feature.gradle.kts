import com.android.build.api.dsl.LibraryExtension
import every.lol.com.configureCoil
import every.lol.com.libs
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("everylol.library")
    id("everylol.compose")
}

extensions.configure<LibraryExtension> {
    packaging {
        resources.excludes.add("META-INF/**")
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

extensions.configure<KotlinMultiplatformExtension> {
    sourceSets.apply {
        getByName("commonMain").dependencies {
            implementation(project(":core:common"))
            implementation(project(":core:data"))
            implementation(project(":core:datastore"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:domain"))
            implementation(project(":core:model"))
            implementation(project(":core:navigation"))
            implementation(project(":core:network"))
            implementation(project(":core:ui"))

            implementation(libs.findLibrary("androidx-navigation-compose").get())
            implementation(libs.findLibrary("precompose-viewmodel").get())
            implementation(libs.findLibrary("precompose-koin").get())
        }
    }
}

configureCoil()